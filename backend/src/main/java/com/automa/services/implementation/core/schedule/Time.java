package com.automa.services.implementation.core.schedule;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.Action;

@Service
@Validated
public class Time {

    public ArrayList<HashMap<String, Object>> runOnce(Action action,
            HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
        action.getData().put("active", false);
        action.getOutput().getFirst().put("status", "success");

        ArrayList<HashMap<String, Object>> output = new ArrayList<>();
        output.add(action.getOutput().getFirst());
        return output;
    }

    public ArrayList<HashMap<String, Object>> runDaily(Action action,
            HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
        action.getData().put("active", false);
        action.getOutput().getFirst().put("status", "success");
        
        ArrayList<HashMap<String, Object>> output = new ArrayList<>();
        output.add(action.getOutput().getFirst());
        return output;
    }
}
