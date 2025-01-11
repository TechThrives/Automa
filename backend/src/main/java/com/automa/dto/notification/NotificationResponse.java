package com.automa.dto.notification;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class NotificationResponse {
    private UUID id;
    private String title;
    private String message;
    private LocalDateTime timestamp;
}
