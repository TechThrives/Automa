package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IWorkflow;

@RestController
@Validated
public class WorkflowController {

    private final IWorkflow workflowService;

    public WorkflowController(IWorkflow workflowService) {
        this.workflowService = workflowService;
    }
}
