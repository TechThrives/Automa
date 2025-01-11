package com.automa.services.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.notification.NotificationRequest;
import com.automa.dto.notification.NotificationResponse;
import com.automa.entity.ApplicationUser;
import com.automa.entity.Notification;
import com.automa.repository.NotificationRepository;
import com.automa.services.interfaces.INotification;
import com.automa.utils.ContextUtils;

@Service
@Validated
public class NotificationService implements INotification {

    private final NotificationRepository notificationRepository;
    private final ApplicationUserService applicationUserService;

    public NotificationService(NotificationRepository notificationRepository,
            ApplicationUserService applicationUserService) {
        this.notificationRepository = notificationRepository;
        this.applicationUserService = applicationUserService;
    }

    @Override
    public NotificationResponse save(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationRequest, notification);

        ApplicationUser user = applicationUserService.findByEmail(notificationRequest.getEmail());
        notification.setUser(user);

        Notification saved = notificationRepository.save(notification);

        NotificationResponse response = new NotificationResponse();
        BeanUtils.copyProperties(saved, response);
        return response;
    }

    @Override
    public List<NotificationResponse> findAll() {
        ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
        List<Notification> notifications = notificationRepository.findByUser(user);

        return notifications.stream().map(notification -> {
            NotificationResponse response = new NotificationResponse();
            BeanUtils.copyProperties(notification, response);
            return response;
        }).toList();
    }

    @Override
    public void deleteById(UUID id) {
        notificationRepository.deleteById(id);
    }

}
