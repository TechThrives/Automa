package com.automa.services.implementation.core;

import java.util.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.automa.entity.ApplicationUser;
import com.automa.entity.Workflow;
import com.automa.entity.action.Action;
import com.automa.entity.credential.GoogleCredential;
import com.automa.repository.WorkflowRepository;
import com.automa.services.implementation.core.mail.GoogleMail;
import com.automa.services.implementation.core.schedule.Time;
import com.automa.utils.ServiceContext;
import com.automa.utils.WorkflowUtils;

@Service
public class WorkflowRunner {

    private final WorkflowRepository workflowRepository;
    private final Time time;
    private final GoogleMail googleMail;

    public WorkflowRunner(WorkflowRepository workflowRepository, Time time, GoogleMail googleMail) {
        this.workflowRepository = workflowRepository;
        this.time = time;
        this.googleMail = googleMail;
    }

    private final List<Workflow> workflows = new ArrayList<>();
    private final Set<String> visitedFlows = new HashSet<>();

    @Scheduled(fixedDelay = 8000)
    public void runScheduledWorkflow() {
        workflows.clear();
        WorkflowUtils.findActionTypesBySchedule().forEach(actionType -> {
            System.out.println("Finding workflows for trigger type: " + actionType);
            workflowRepository.findByTrigger_Type(actionType).forEach(workflow -> {
                if (WorkflowUtils.runNow(workflow.getTrigger().getData(), actionType))
                    workflows.add(workflow);
            });
        });

        System.out.println("Found " + workflows.size() + " workflows to run.");

        workflows.forEach(this::runWorkflow);
    }

    public void runWorkflow(Workflow workflow) {
        ApplicationUser user = workflow.getUser();
        GoogleCredential googleCredentials = user.getGoogleCredential();
        if (googleCredentials != null) {
            ServiceContext.setGoogleCredential(googleCredentials);
            ServiceContext.setUsername(user.getEmail());
            visitedFlows.clear(); // Clear visited flows for each workflow execution
            Action trigger = workflow.getTrigger();
            if (trigger != null) {
                executeNextActions(trigger, new HashMap<>());
            }
            ServiceContext.clearContext();
            workflowRepository.save(workflow);

        } else {
            System.out.println("Google credentials not found for user: " + user.getEmail());
        }
    }

    private void executeNextActions(Action currentAction, HashMap<String, Object> previousOutput) {
        if (currentAction == null) {
            return;
        }

        HashMap<String, Object> currentOutput = runAction(currentAction, previousOutput);

        currentAction.getOutgoingFlows().forEach(flow -> {
            Action nextAction = flow.getTarget();

            String flowKey = currentAction.getId() + "->" + flow.getId();

            if (!visitedFlows.contains(flowKey) && nextAction != null) {
                visitedFlows.add(flowKey);
                executeNextActions(nextAction, currentOutput);
            }
        });
    }

    public HashMap<String, Object> runAction(Action action, HashMap<String, Object> previousOutput) {
        System.out.println("Executing action of type: " + action.getType() + " with data: " + action.getData());
        HashMap<String, Object> output = new HashMap<>();

        switch (action.getType()) {
            case RUNONCE:
                output = time.runOnce(action, previousOutput);
                break;

            case RUNDAILY:
                output = time.runDaily(action, previousOutput);
                break;

            case SENDMAIL:
                output = googleMail.sendMail(action, previousOutput);
                break;

            default:
                System.out.println("Unsupported action type: " + action.getType());
                break;
        }

        return output;
    }
}
