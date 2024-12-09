package com.automa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;
import com.automa.services.interfaces.IActionInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@Validated
@RequestMapping("/api/info")
public class ActionInfoController {

    private final IActionInfo actionInfoService;

    public ActionInfoController(IActionInfo actionInfoService) {
        this.actionInfoService = actionInfoService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActionInfo>> getAll() {
        return new ResponseEntity<>(actionInfoService.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/type/trigger")
    public ResponseEntity<List<ActionInfo>> getTriggers() {
        return new ResponseEntity<>(actionInfoService.getTriggers(), HttpStatus.OK);
    }

    @GetMapping("/type/action")
    public ResponseEntity<List<ActionInfo>> getActions() {
        return new ResponseEntity<>(actionInfoService.getActions(), HttpStatus.OK);
    }

    @GetMapping("/actionType/{actionType}")
    public ResponseEntity<ActionInfo> getByActionType(@PathVariable ActionType actionType) {
        return new ResponseEntity<>(actionInfoService.getByActionType(actionType), HttpStatus.OK);
    }

    @GetMapping("/group/{actionGroup}")
    public ResponseEntity<List<ActionInfo>> getByGroup(@PathVariable ActionGroup actionGroup) {
        return new ResponseEntity<>(actionInfoService.getByActionGroup(actionGroup), HttpStatus.OK);
    }

    @GetMapping("/group/triggers")
    public ResponseEntity<Map<ActionGroup, List<ActionInfo>>> getGroupedTriggers() {
        return new ResponseEntity<>(actionInfoService.getGroupedTriggers(), HttpStatus.OK);
    }

    @GetMapping("/group/actions")
    public ResponseEntity<Map<ActionGroup, List<ActionInfo>>> getGroupedActions() {
        return new ResponseEntity<>(actionInfoService.getGroupedActions(), HttpStatus.OK);
    }
 
}
