package com.automa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.dto.workflow.WorkflowResponse;

public interface IWorkflow {
    public List<WorkflowResponse> findAll();
    public WorkflowRequestResponse findById(UUID id);
    public WorkflowRequestResponse save(WorkflowRequestResponse request);
}
