package com.automa.dto.workflow;

import java.util.List;
import java.util.UUID;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.entity.action.ActionType;

import lombok.Data;

@Data
public class WorkflowResponse {
    private UUID id;
    private String name;
    private List<ActionType> actions;
    private Integer runs;
    private ActionRequestResponse trigger;
    private Boolean isActive;
    private String user;
}
