package com.esoft.api.dto.batch;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BatchStudentRequest(
        @NotNull(message = "Student ID is required")
        UUID studentId
) {
}
