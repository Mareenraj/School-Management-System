package com.esoft.api.dto.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID studentId,
        String studentName,
        String message,
        double averageScore,
        boolean read,
        LocalDateTime createdAt
) {
}
