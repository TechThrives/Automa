package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.ITrigger;

@RestController
@Validated
@RequestMapping("/api/trigger")
public class TriggerController {

    private final ITrigger triggerService;

    public TriggerController(ITrigger triggerService) {
        this.triggerService = triggerService;
    }

    @GetMapping("/path")
    public String getMethodName() {
        return new String();
    }
}
