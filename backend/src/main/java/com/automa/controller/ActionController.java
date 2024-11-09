package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IAction;

@RestController
@Validated
public class ActionController {

    private final IAction actionService;

    public ActionController(IAction actionService) {
        this.actionService = actionService;
    }
}
