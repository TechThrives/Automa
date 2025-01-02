package com.automa.services.implementation.core.schedule;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.Action;

@Service
@Validated
public class Time {

    public HashMap<String, Object> runOnce(Action action, HashMap<String, Object> previouseOutput) {
        action.getData().put("active", false);
        action.getOutput().put("status", "success");
        return  action.getOutput();
    }

    public HashMap<String, Object> runDaily(Action action, HashMap<String, Object> previouseOutput) {
        action.getData().put("active", false);
        action.getOutput().put("status", "success");
        return  action.getOutput();
    }
}
