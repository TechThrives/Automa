package com.automa.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.action.ActionRequestResponse;
import com.automa.services.implementation.core.WorkflowRunner;
import com.automa.services.interfaces.IAction;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/action")
public class ActionController {

    private final IAction actionService;
    public ActionController(IAction actionService, WorkflowRunner workflowRunner) {
        this.actionService = actionService;
    }

    @PostMapping("/test")
    public ResponseEntity<ArrayList<HashMap<String, Object>>> test(@Valid @RequestBody ActionRequestResponse actionRequestResponse) {
        return new ResponseEntity<>(actionService.runAction(actionRequestResponse), HttpStatus.OK);
    }

    @GetMapping("/spreadsheets/all")
    public ResponseEntity<HashMap<String, Object>> getAllSpreadSheets() {
        return new ResponseEntity<>(actionService.getAllSpreadSheets(), HttpStatus.OK);
    }

    @GetMapping("/spreadsheets/{id}/sheets")
    public ResponseEntity<HashMap<String, Object>> getAllSheets(@PathVariable String id) {
        return new ResponseEntity<>(actionService.getAllSheets(id), HttpStatus.OK);
    }
}
