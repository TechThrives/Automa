package com.automa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.dto.workflow.WorkflowResponse;

public interface IWorkflow {
    List<WorkflowResponse> findAll();
    WorkflowRequestResponse findById(UUID id);
    WorkflowRequestResponse save(WorkflowRequestResponse request);
}
