package com.automa.dto.notification;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationRequest {
    private String email;
    private String title;
    private String message;
    private LocalDateTime timestamp;
}
