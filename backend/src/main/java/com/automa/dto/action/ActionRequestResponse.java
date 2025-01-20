package com.automa.dto.action;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.automa.dto.position.PositionRequestResponse;
import com.automa.entity.action.ActionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionRequestResponse {
    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Action type is required")
    private ActionType type;

    @NotNull(message = "Position is required")
    @Valid
    private PositionRequestResponse position;

    @NotNull(message = "Data cannot be null")
    private HashMap<String, Object> data;

    private List<HashMap<String, Object>> output;
}
