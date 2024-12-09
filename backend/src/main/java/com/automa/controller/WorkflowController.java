package com.automa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.services.interfaces.IWorkflow;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final IWorkflow workflowService;

    public WorkflowController(IWorkflow workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/save")
    public ResponseEntity<WorkflowRequestResponse> saveWorkflow(@Valid @RequestBody WorkflowRequestResponse request) {
        return new ResponseEntity<>(workflowService.saveWorkflow(request), HttpStatus.OK);
    }
}
