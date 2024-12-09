package com.automa.dto.flow;

import java.util.UUID;

import com.automa.entity.flow.FlowType;

import lombok.Data;

@Data
public class FlowRequestResponse {
    private UUID id;
    private UUID source;;
    private UUID target;
    private FlowType type;
}
