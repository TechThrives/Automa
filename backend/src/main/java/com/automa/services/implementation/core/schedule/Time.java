package com.automa.services.implementation.core.schedule;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class Time {

    public HashMap<String, Object> runOnce(HashMap<String, Object> data, HashMap<String, Object> output) {
        data.put("active", false);
        output.put("status", "success");
        return output;
    }

    public HashMap<String, Object> runDaily(HashMap<String, Object> data, HashMap<String, Object> output) {
        data.put("active", false);
        output.put("status", "success");
        return output;
    }
}
