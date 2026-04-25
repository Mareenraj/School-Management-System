package com.esoft.api.dto.student;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentRequest(
        @NotNull(message = "User ID is required")
        UUID userId,

        UUID batchId
) {
}
