package com.automa.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.notification.NotificationResponse;
import com.automa.services.implementation.NotificationService;

@RestController
@Validated
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return new ResponseEntity<>(notificationService.findAll(), HttpStatus.OK);
    }
    

    @DeleteMapping("/{id}")
    public void deleteNotification(@Validated @PathVariable UUID id) {
        notificationService.deleteById(id);
    }
}
