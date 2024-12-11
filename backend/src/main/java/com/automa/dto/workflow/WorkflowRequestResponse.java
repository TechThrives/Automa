package com.automa.dto.workflow;

import java.util.List;
import java.util.UUID;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.dto.flow.FlowRequestResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowRequestResponse {
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Nodes cannot be null")
    private List<@Valid ActionRequestResponse> nodes;

    @NotNull(message = "Edges cannot be null")
    private List<@Valid FlowRequestResponse> edges;
}
