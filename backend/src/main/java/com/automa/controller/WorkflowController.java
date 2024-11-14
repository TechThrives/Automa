package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IWorkflow;

@RestController
@Validated
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final IWorkflow workflowService;

    public WorkflowController(IWorkflow workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/path")
    public String getMethodName() {
        return new String();
    }
}
