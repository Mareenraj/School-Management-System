package com.esoft.api.dto.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AssignmentRequest(
        @NotNull(message = "Module ID is required")
        UUID moduleId,

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Due date is required")
        LocalDate dueDate
) {
}
