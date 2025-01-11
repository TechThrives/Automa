package com.automa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.automa.dto.notification.NotificationRequest;
import com.automa.dto.notification.NotificationResponse;

public interface INotification {
    NotificationResponse save(NotificationRequest notification);

    List<NotificationResponse> findAll();

    void deleteById(UUID id);
}
