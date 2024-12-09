package com.automa.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.dto.flow.FlowRequestResponse;
import com.automa.dto.position.PositionRequestResponse;
import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.entity.ApplicationUser;
import com.automa.entity.Workflow;
import com.automa.entity.action.Action;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.Position;
import com.automa.entity.flow.Flow;
import com.automa.repository.WorkflowRepository;
import com.automa.services.interfaces.IWorkflow;
import com.automa.utils.ContextUtils;

@Service
@Validated
public class WorkflowService implements IWorkflow {

    private final WorkflowRepository workflowRepository;
    private final ApplicationUserService applicationUserService;
    private final ActionInfoService actionInfoService;

    public WorkflowService(WorkflowRepository workflowRepository,
            ApplicationUserService applicationUserService,
            ActionService actionService,
            ActionInfoService actionInfoService) {
        this.workflowRepository = workflowRepository;
        this.applicationUserService = applicationUserService;
        this.actionInfoService = actionInfoService;
    }

    @Override
    public WorkflowRequestResponse saveWorkflow(WorkflowRequestResponse request) {

        Workflow workflow = new Workflow();
        workflow.setName("Workflow");
        ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
        workflow.setUser(user);

        List<Action> actions = new ArrayList<>();
        List<Flow> flows = new ArrayList<>();

        for (ActionRequestResponse actionRequest : request.getNodes()) {
            ActionInfo actionInfos = actionInfoService.getByActionType(actionRequest.getType());

            // Validate the request data against the required keys in ActionInfo
            for (String key : actionInfos.getData().keySet()) {
                if (!actionRequest.getData().containsKey(key)) {
                    throw new RuntimeException("Missing key " + key + " in " + actionInfos.getName());
                }
            }

            Action action = new Action();
            BeanUtils.copyProperties(actionRequest, action);
            action.setOutput(actionInfos.getOutput());
            action.setWorkflow(workflow);

            Position position = new Position();
            BeanUtils.copyProperties(actionRequest.getPosition(), position);
            position.setAction(action);
            action.setPosition(position);
            actions.add(action);
        }

        for (FlowRequestResponse flowRequest : request.getEdges()) {
            Flow flow = new Flow();
            Action targetAction = actions.stream().filter(a -> a.getId().equals(flowRequest.getTarget())).findFirst().get();
            Action sourceAction = actions.stream().filter(a -> a.getId().equals(flowRequest.getSource())).findFirst().get();
            flow.setType(flowRequest.getType());
            flow.setSource(sourceAction);
            flow.setTarget(targetAction);
            flow.setWorkflow(workflow);
            flows.add(flow);
        }

        workflow.setActions(actions);
        workflow.setFlows(flows);

        workflowRepository.save(workflow);

        // Build the response
        WorkflowRequestResponse response = new WorkflowRequestResponse();
    BeanUtils.copyProperties(workflow, response);

    // Map actions to nodes
    List<ActionRequestResponse> nodes = actions.stream().map(action -> {
        ActionRequestResponse node = new ActionRequestResponse();
        BeanUtils.copyProperties(action, node);
        PositionRequestResponse positionResponse = new PositionRequestResponse();
        BeanUtils.copyProperties(action.getPosition(), positionResponse);
        node.setPosition(positionResponse);
        return node;
    }).collect(Collectors.toList());

    // Map flows to edges
    List<FlowRequestResponse> edges = flows.stream().map(flow -> {
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
