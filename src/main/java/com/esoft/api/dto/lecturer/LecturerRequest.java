package com.esoft.api.dto.lecturer;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LecturerRequest(
        @NotNull(message = "User ID is required")
        UUID userId
) {
}
