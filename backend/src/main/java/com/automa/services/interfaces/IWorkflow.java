package com.automa.services.interfaces;

import java.util.UUID;

import com.automa.dto.workflow.WorkflowRequestResponse;

public interface IWorkflow {

    public WorkflowRequestResponse saveWorkflow(WorkflowRequestResponse request);
    public WorkflowRequestResponse getWorkflow(UUID id);
}
