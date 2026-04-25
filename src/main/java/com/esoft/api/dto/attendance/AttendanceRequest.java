package com.esoft.api.dto.attendance;

import com.esoft.api.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceRequest(
        @NotNull(message = "Student ID is required")
        UUID studentId,

        @NotNull(message = "Module ID is required")
        UUID moduleId,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Status is required")
        AttendanceStatus status
) {
}
