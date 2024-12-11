package com.automa.dto.flow;

import java.util.UUID;

import com.automa.entity.flow.FlowType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlowRequestResponse {
    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotNull(message = "Source cannot be null")
    private UUID source;

    @NotNull(message = "Target cannot be null")
    private UUID target;

    @NotNull(message = "Type cannot be null")
    private FlowType type;
}
