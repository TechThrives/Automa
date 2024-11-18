package com.automa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
    private List<String> details;

    public MessageResponse(String message) {
        this.message = message;
    }
}