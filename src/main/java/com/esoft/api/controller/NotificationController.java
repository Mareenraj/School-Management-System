package com.esoft.api.controller;

import com.esoft.api.dto.notification.NotificationResponse;
import com.esoft.api.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NotificationResponse>> getByStudentId(@PathVariable UUID studentId) {
        return ResponseEntity.ok(notificationService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadByStudentId(@PathVariable UUID studentId) {
        return ResponseEntity.ok(notificationService.getUnreadByStudentId(studentId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}
