package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.ITrigger;

@RestController
@Validated
public class TriggerController {

    private final ITrigger triggerService;

    public TriggerController(ITrigger triggerService) {
        this.triggerService = triggerService;
    }
}
