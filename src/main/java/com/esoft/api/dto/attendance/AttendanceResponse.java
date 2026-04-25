package com.esoft.api.dto.attendance;

import com.esoft.api.entity.enums.AttendanceStatus;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceResponse(
        UUID id,
        UUID studentId,
        String studentName,
        UUID moduleId,
        String moduleName,
        LocalDate date,
        AttendanceStatus status
) {
}
