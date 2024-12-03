package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IActionInfo;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@Validated
@RequestMapping("/api/info")
public class ActionInfoController {

    private final IActionInfo actionInfoService;

    public ActionInfoController(IActionInfo actionInfoService) {
        this.actionInfoService = actionInfoService;
    }
    
}
