package com.automa.services.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.dto.flow.FlowRequestResponse;
import com.automa.dto.position.PositionRequestResponse;
import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.dto.workflow.WorkflowResponse;
import com.automa.entity.Workflow;
import com.automa.entity.action.Action;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;
import com.automa.entity.action.BaseType;
import com.automa.entity.flow.Flow;
import com.automa.repository.ActionRepository;
import com.automa.repository.FlowRepository;
import com.automa.repository.WorkflowRepository;
import com.automa.services.interfaces.IWorkflow;
import com.automa.utils.ContextUtils;
import com.automa.utils.WorkflowUtils;

@Service
@Validated
public class WorkflowService implements IWorkflow {

    private final WorkflowRepository workflowRepository;
    private final ApplicationUserService applicationUserService;
    private final ActionRepository actionRepository;
    private final ActionInfoService actionInfoService;
    private final FlowRepository flowRepository;

    public WorkflowService(WorkflowRepository workflowRepository,
            ApplicationUserService applicationUserService,
            ActionInfoService actionInfoService,
            ActionRepository actionRepository,
            FlowRepository flowRepository) {
        this.workflowRepository = workflowRepository;
        this.applicationUserService = applicationUserService;
        this.actionInfoService = actionInfoService;
        this.flowRepository = flowRepository;
        this.actionRepository = actionRepository;
    }

    @Override
    public WorkflowRequestResponse findById(UUID id) {

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        // Build the response
        WorkflowRequestResponse response = new WorkflowRequestResponse();
        BeanUtils.copyProperties(workflow, response);

        // Map actions to nodes
        List<ActionRequestResponse> nodes = workflow.getActions().stream().map(action -> {
            ActionRequestResponse node = new ActionRequestResponse();
            BeanUtils.copyProperties(action, node);
            PositionRequestResponse positionResponse = new PositionRequestResponse();
            positionResponse.setX(action.getPositionX());
            positionResponse.setY(action.getPositionY());
            node.setPosition(positionResponse);
            return node;
        }).collect(Collectors.toList());

        // Map flows to edges
        List<FlowRequestResponse> edges = workflow.getFlows().stream().map(flow -> {
            FlowRequestResponse edge = new FlowRequestResponse();
            BeanUtils.copyProperties(flow, edge);
            edge.setSource(flow.getSource().getId());
            edge.setTarget(flow.getTarget().getId());
            return edge;
        }).collect(Collectors.toList());

        response.setNodes(nodes);
        response.setEdges(edges);

        return response;
    }

    @Override
    public List<WorkflowResponse> findAll() {
        List<Workflow> workflows = workflowRepository.findAll();

        return workflows.stream().map(workflow -> {
            WorkflowResponse response = new WorkflowResponse();
            BeanUtils.copyProperties(workflow, response);
            response.setUser(workflow.getUser().getUsername());
            List<ActionType> actions = workflow.getActions()
                    .stream().map(action -> action.getType()).collect(Collectors.toList());
            response.setActions(actions);
            ActionRequestResponse trigger = new ActionRequestResponse();
            if (workflow.getTrigger() != null) {
                trigger.setId(workflow.getTrigger().getId());
                trigger.setType(workflow.getTrigger().getType());
                trigger.setData(workflow.getTrigger().getData());
            }
            response.setTrigger(trigger);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkflowRequestResponse save(WorkflowRequestResponse request) {

        for (ActionRequestResponse node : request.getNodes()) {
            ActionInfo actionInfos = actionInfoService.getByActionType(node.getType());

            for (String key : actionInfos.getData().keySet()) {
                if (!node.getData().containsKey(key)) {
                    throw new RuntimeException("Missing key " + key + " in " + actionInfos.getName());
                }
            }
        }

        int triggerCount = 0;
        int actionCount = 0;
        Action trigger = null;

        List<ActionInfo> triggerActionInfos = actionInfoService.getTriggers();
        List<ActionType> triggers = triggerActionInfos.stream()
                .map(ActionInfo::getActionType)
                .collect(Collectors.toList());

        for (ActionRequestResponse node : request.getNodes()) {
            if (triggers.contains(node.getType())) {
                triggerCount++;
            } else {
                actionCount++;
            }
        }

        if (triggerCount != 1) {
            throw new RuntimeException("Workflow must contain exactly one trigger.");
        }

        if (actionCount == 0) {
            throw new RuntimeException("Workflow must contain at least one action.");
        }

        // Fetch existing workflow if it exists
        Workflow workflow = workflowRepository.findById(request.getId())
                .orElse(new Workflow());
        workflow.setUser(applicationUserService.findByEmail(ContextUtils.getUsername()));
        workflow.setName(request.getName());
        workflow.setIsActive(true);

        workflow.getActions().clear();
        workflow.getFlows().clear();
        workflow.setTrigger(null);

        workflowRepository.save(workflow);

        Map<UUID, Action> actionMap = new HashMap<>();
        for (ActionRequestResponse node : request.getNodes()) {
            Action action = new Action();
            action.setId(node.getId());
            action.setType(node.getType());
            action.setPositionX(node.getPosition().getX());
            action.setPositionY(node.getPosition().getY());
            action.setData(node.getData());
            action.setOutput(node.getOutput());
            action.setWorkflow(workflow);

            workflow.getActions().add(action);
            if (triggers.contains(action.getType())) {
                trigger = action;
            }
            actionMap.put(node.getId(), action);
        }

        for (FlowRequestResponse edge : request.getEdges()) {
            if (!WorkflowUtils.isValidConnection(edge.getSource(), edge.getTarget(), workflow.getFlows())) {
                throw new RuntimeException("Workflow must not create a cycle.");
            }

            Flow flow = new Flow();
            flow.setId(edge.getId());
            flow.setType(edge.getType());
            flow.setSource(actionMap.get(edge.getSource()));
            flow.setTarget(actionMap.get(edge.getTarget()));
            flow.setWorkflow(workflow);
            workflow.getFlows().add(flow);
        }

        workflow.setTrigger(trigger);

        // Save workflow with cascaded actions and flows
        workflow = workflowRepository.save(workflow);

        // Build and return the response
        WorkflowRequestResponse response = new WorkflowRequestResponse();
        BeanUtils.copyProperties(workflow, response);

        // Map actions to nodes
        List<ActionRequestResponse> nodesResponse = workflow.getActions().stream().map(action -> {
            ActionRequestResponse node = new ActionRequestResponse();
            BeanUtils.copyProperties(action, node);
            PositionRequestResponse positionResponse = new PositionRequestResponse();
            positionResponse.setX(action.getPositionX());
            positionResponse.setY(action.getPositionY());
            node.setPosition(positionResponse);
            return node;
        }).collect(Collectors.toList());

        // Map flows to edges
        List<FlowRequestResponse> edgesResponse = workflow.getFlows().stream().map(flow -> {
            FlowRequestResponse edge = new FlowRequestResponse();
            BeanUtils.copyProperties(flow, edge);
            edge.setSource(flow.getSource().getId());
            edge.setTarget(flow.getTarget().getId());
            return edge;
        }).collect(Collectors.toList());

        response.setNodes(nodesResponse);
        response.setEdges(edgesResponse);

        return response;
    }

    @Override
    public List<WorkflowResponse> findByUser() {
        List<Workflow> workflows = workflowRepository.findByUser_Email(ContextUtils.getUsername());
        System.out.println("Found total " + workflows.size() + " workflows.");
        System.out.println("Found total " + actionRepository.findAll().size() + " actions.");
        System.out.println("Found total " + flowRepository.findAll().size() + " flows.");

        return workflows.stream().map(workflow -> {
            WorkflowResponse response = new WorkflowResponse();
            BeanUtils.copyProperties(workflow, response);
            response.setUser(workflow.getUser().getUsername());
            List<ActionType> actions = workflow.getActions()
                    .stream().map(action -> action.getType()).collect(Collectors.toList());
            response.setActions(actions);
            ActionRequestResponse trigger = new ActionRequestResponse();
            if (workflow.getTrigger() != null) {
                trigger.setId(workflow.getTrigger().getId());
                trigger.setType(workflow.getTrigger().getType());
                trigger.setData(workflow.getTrigger().getData());
            }
            response.setTrigger(trigger);
            return response;
        }).collect(Collectors.toList());
    }

}
