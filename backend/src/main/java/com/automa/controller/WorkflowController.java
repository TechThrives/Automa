package com.automa.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.workflow.WorkflowRequestResponse;
import com.automa.dto.workflow.WorkflowResponse;
import com.automa.services.interfaces.IWorkflow;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@Validated
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final IWorkflow workflowService;

    public WorkflowController(IWorkflow workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowRequestResponse> getWorkflow(@Valid @PathVariable UUID id) {
        return new ResponseEntity<>(workflowService.findById(id), HttpStatus.OK);
    }
    
    @PostMapping("/save")
    public ResponseEntity<WorkflowRequestResponse> saveWorkflow(@Valid @RequestBody WorkflowRequestResponse request) {
        return new ResponseEntity<>(workflowService.save(request), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<WorkflowResponse>> getUserWorkflow() {
        return new ResponseEntity<>(workflowService.findByUser(), HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<WorkflowResponse>> getWorkflows() {
        return new ResponseEntity<>(workflowService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<WorkflowResponse> toggleActive(@Valid @PathVariable UUID id, boolean isActive) {
        return new ResponseEntity<>(workflowService.toggleActive(id, isActive), HttpStatus.OK);
    }
    
}
