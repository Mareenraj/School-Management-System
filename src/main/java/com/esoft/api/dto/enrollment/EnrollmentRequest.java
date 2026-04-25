package com.esoft.api.dto.enrollment;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollmentRequest(
        @NotNull(message = "Student ID is required")
        UUID studentId,

        @NotNull(message = "Module ID is required")
        UUID moduleId
) {
}
