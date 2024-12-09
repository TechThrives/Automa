package com.automa.dto.workflow;

import java.util.List;
import java.util.UUID;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.dto.flow.FlowRequestResponse;

import lombok.Data;

@Data
public class WorkflowRequestResponse {
    private UUID id = UUID.randomUUID();
    private String name;
    private List<ActionRequestResponse> nodes;
    private List<FlowRequestResponse> edges;
}
