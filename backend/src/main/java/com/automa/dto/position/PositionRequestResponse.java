package com.automa.dto.position;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PositionRequestResponse {
    @NotNull(message = "Position cannot be null")
    private Float x;

    @NotNull(message = "Position cannot be null")
    private Float y;
}
