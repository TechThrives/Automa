package com.automa.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IAction;

@RestController
@Validated
@RequestMapping("/api/action")
public class ActionController {

    private final IAction actionService;

    public ActionController(IAction actionService) {
        this.actionService = actionService;
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
