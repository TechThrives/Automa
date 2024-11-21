package com.automa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.entity.action.Action;
import com.automa.services.interfaces.IAction;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@Validated
@RequestMapping("/api/action")
public class ActionController {

    private final IAction actionService;

    public ActionController(IAction actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/path")
    public ResponseEntity<List<Action>> getMethodName() {
        return new ResponseEntity<>(actionService.runMethod(), HttpStatus.OK);
    }
    
}
