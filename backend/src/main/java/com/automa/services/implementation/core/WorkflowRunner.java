package com.automa.services.implementation.core;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.automa.dto.notification.NotificationRequest;
import com.automa.entity.ApplicationUser;
import com.automa.entity.Workflow;
import com.automa.entity.action.Action;
import com.automa.entity.credential.GoogleCredential;
import com.automa.repository.ApplicationUserRepository;
import com.automa.repository.WorkflowRepository;
import com.automa.services.implementation.NotificationService;
import com.automa.services.implementation.core.mail.GoogleMail;
import com.automa.services.implementation.core.schedule.Time;
import com.automa.utils.ServiceContext;
import com.automa.utils.WorkflowUtils;

@Service
public class WorkflowRunner {

    private final WorkflowRepository workflowRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final NotificationService notificationService;
    private final Time time;
    private final GoogleMail googleMail;

    public WorkflowRunner(WorkflowRepository workflowRepository,
            ApplicationUserRepository applicationUserRepository,
            NotificationService notificationService,
            Time time, GoogleMail googleMail) {
        this.workflowRepository = workflowRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.notificationService = notificationService;
        this.time = time;
        this.googleMail = googleMail;
    }

    @Value("${workflow.cost}")
    private Integer workflowCost;

    private final List<Workflow> workflows = new ArrayList<>();
    private final Set<String> visitedFlows = new HashSet<>();

    // Run scheduled workflows
    @Scheduled(fixedDelay = 8000)
    public void runScheduledWorkflow() {
        workflows.clear();
        WorkflowUtils.findActionTypesBySchedule().forEach(actionType -> {
            System.out.println("Finding workflows for trigger type: " + actionType);
            workflowRepository.findByTrigger_Type(actionType).forEach(workflow -> {
                if (WorkflowUtils.runNow(workflow.getTrigger().getData(), actionType) && workflow.getIsActive())
                    workflows.add(workflow);
            });
        });

        System.out.println("Found " + workflows.size() + " workflows to run.");

        workflows.forEach(this::runWorkflow);
    }

    public void runWorkflow(Workflow workflow) {
        ApplicationUser user = workflow.getUser();
        if (user.getCredits() >= workflowCost) {
            GoogleCredential googleCredentials = user.getGoogleCredential();
            if (googleCredentials != null) {
                ServiceContext.setGoogleCredential(googleCredentials);
                ServiceContext.setUsername(user.getEmail());
                visitedFlows.clear(); // Clear visited flows for each workflow execution
                Action trigger = workflow.getTrigger();
                if (trigger != null) {
                    HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput = new HashMap<>();
                    executeNextActions(trigger, workflowOutput);
                }

                ServiceContext.clearContext();

                // Deduct credits from user on workflow completion
                user.setCredits(user.getCredits() - workflowCost);
                applicationUserRepository.save(user);

            } else {
                System.out.println("Google credentials not found for user: " + user.getEmail());

                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setEmail(user.getEmail());
                notificationRequest.setTitle("Workflow Error: " + workflow.getName());
                notificationRequest.setMessage("Google credentials not configured.");
                notificationService.save(notificationRequest);

                workflow.setIsActive(false);
            }
        } else {
            System.out.println("Insufficient credits for user: " + user.getEmail());

            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setEmail(user.getEmail());
            notificationRequest.setTitle("Workflow Error: " + workflow.getName());
            notificationRequest.setMessage("Insufficient credits.");
            notificationService.save(notificationRequest);

            workflow.setIsActive(false);
        }

        workflow.setRuns(workflow.getRuns() + 1);
        workflowRepository.save(workflow);
    }

    private void executeNextActions(Action currentAction,
            HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
        if (currentAction == null) {
            return;
        }

        ArrayList<HashMap<String, Object>> currentOutput = runAction(currentAction, workflowOutput);

        workflowOutput.put(currentAction.getName(), currentOutput);

        currentAction.getOutgoingFlows().forEach(flow -> {
            Action nextAction = flow.getTarget();

            String flowKey = currentAction.getId() + "->" + flow.getId();

            if (!visitedFlows.contains(flowKey) && nextAction != null) {
                visitedFlows.add(flowKey);
                executeNextActions(nextAction, workflowOutput);
            }
        });
    }

    public ArrayList<HashMap<String, Object>> runAction(Action action,
            HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
        ArrayList<HashMap<String, Object>> output = new ArrayList<>();

        System.out.println("Running action: " + action.getName());

        switch (action.getType()) {
            case RUNONCE:
                output = time.runOnce(action, workflowOutput);
                break;

            case RUNDAILY:
                output = time.runDaily(action, workflowOutput);
                break;

            case SENDMAIL:
                output = googleMail.sendMail(action, workflowOutput);
                break;

            default:
                System.out.println("Unsupported action type: " + action.getType());
                break;
        }

        return output;
    }
}
