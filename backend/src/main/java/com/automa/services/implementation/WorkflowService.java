package com.automa.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.dto.flow.FlowRequestResponse;
import com.automa.dto.position.PositionRequestResponse;
import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.dto.workflow.WorkflowResponse;
import com.automa.entity.ApplicationUser;
import com.automa.entity.Workflow;
import com.automa.entity.action.Action;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;
import com.automa.entity.action.BaseType;
import com.automa.entity.action.Position;
import com.automa.entity.flow.Flow;
import com.automa.repository.WorkflowRepository;
import com.automa.services.interfaces.IWorkflow;
import com.automa.utils.ContextUtils;
import com.automa.utils.WorkflowUtils;

import jakarta.transaction.Transactional;

@Service
@Validated
public class WorkflowService implements IWorkflow {

    private final WorkflowRepository workflowRepository;
    private final ApplicationUserService applicationUserService;
    private final ActionService actionService;
    private final ActionInfoService actionInfoService;
    private final FlowService flowService;
    private final PositionService positionService;

    public WorkflowService(WorkflowRepository workflowRepository,
            ApplicationUserService applicationUserService,
            ActionService actionService,
            ActionInfoService actionInfoService,
            FlowService flowService,
            PositionService positionService) {
        this.workflowRepository = workflowRepository;
        this.applicationUserService = applicationUserService;
        this.actionService = actionService;
        this.actionInfoService = actionInfoService;
        this.flowService = flowService;
        this.positionService = positionService;
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
            BeanUtils.copyProperties(action.getPosition(), positionResponse);
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
            List<ActionType> nodes = workflow.getActions()
            .stream().map(action -> action.getType()).collect(Collectors.toList());
            response.setNodes(nodes);
            ActionRequestResponse trigger = new ActionRequestResponse();
            if(workflow.getTrigger() != null) {
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

        Workflow workflow = workflowRepository.findById(request.getId()).orElseGet(() -> new Workflow());
        workflow.setName(request.getName());
        ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
        workflow.setUser(user);

        List<Action> existingActions = new ArrayList<>(workflow.getActions());
        List<Flow> existingFlows = new ArrayList<>(workflow.getFlows());

        List<Action> updatedActions = new ArrayList<>();
        List<Flow> updatedFlows = new ArrayList<>();

        int triggerCount = 0;
        int actionCount = 0;

        Action trigger = null;

        for (ActionRequestResponse actionRequest : request.getNodes()) {
            ActionInfo actionInfos = actionInfoService.getByActionType(actionRequest.getType());

            // Validate the request data against the required keys in ActionInfo
            for (String key : actionInfos.getData().keySet()) {
                if (!actionRequest.getData().containsKey(key)) {
                    throw new RuntimeException("Missing key " + key + " in " + actionInfos.getName());
                }
            }

            Action action = actionService.findById(actionRequest.getId());
            BeanUtils.copyProperties(actionRequest, action);
            action.setOutput(actionInfos.getOutput());
            action.setWorkflow(workflow);

            Position position = positionService.findByActionId(action.getId());
            BeanUtils.copyProperties(actionRequest.getPosition(), position);
            position.setAction(action);
            action.setPosition(position);

            updatedActions.add(action);

            if (actionInfos.getType() == BaseType.TRIGGER) {
                triggerCount++;
                trigger = action;
            }

            if (actionInfos.getType() == BaseType.ACTION) {
                actionCount++;
            }
        }

        if (triggerCount != 1) {
            throw new RuntimeException("Workflow must contain exactly one trigger.");
        }

        if (actionCount == 0) {
            throw new RuntimeException("Workflow must contain at least one action.");
        }

        for (FlowRequestResponse flowRequest : request.getEdges()) {
            if (!WorkflowUtils.isValidConnection(flowRequest.getSource(), flowRequest.getTarget(), updatedFlows)) {
                throw new RuntimeException("Workflow must not create a cycle.");
            }
            Flow flow = flowService.findById(flowRequest.getId());
            Action sourceAction = updatedActions.stream()
                    .filter(a -> a.getId().equals(flowRequest.getSource()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Source action not found"));
            Action targetAction = updatedActions.stream()
                    .filter(a -> a.getId().equals(flowRequest.getTarget()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Target action not found"));
            flow.setType(flowRequest.getType());
            flow.setSource(sourceAction);
            flow.setTarget(targetAction);
            flow.setWorkflow(workflow);
            updatedFlows.add(flow);
        }

        existingActions.stream()
                .filter(action -> !updatedActions.contains(action))
                .forEach(action -> actionService.delete(action.getId()));

        existingFlows.stream()
                .filter(flow -> !updatedFlows.contains(flow))
                .forEach(flow -> flowService.delete(flow.getId()));

        workflow.setActions(updatedActions);
        workflow.setFlows(updatedFlows);
        workflow.setTrigger(trigger);

        workflow = workflowRepository.save(workflow);

        // Build the response
        WorkflowRequestResponse response = new WorkflowRequestResponse();
        BeanUtils.copyProperties(workflow, response);

        // Map actions to nodes
        List<ActionRequestResponse> nodes = workflow.getActions().stream().map(action -> {
            ActionRequestResponse node = new ActionRequestResponse();
            BeanUtils.copyProperties(action, node);
            PositionRequestResponse positionResponse = new PositionRequestResponse();
            BeanUtils.copyProperties(action.getPosition(), positionResponse);
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

}
