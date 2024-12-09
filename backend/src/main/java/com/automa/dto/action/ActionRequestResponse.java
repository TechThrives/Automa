package com.automa.dto.action;

import java.util.HashMap;
import java.util.UUID;

import com.automa.dto.position.PositionRequestResponse;
import com.automa.entity.action.ActionType;

import lombok.Data;

@Data
public class ActionRequestResponse {
    private UUID id;
    private ActionType type;
    private PositionRequestResponse position;
    private HashMap<String, Object> data;
}
