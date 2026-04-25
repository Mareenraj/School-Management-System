package com.esoft.api.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ModuleRequest(
        @NotBlank(message = "Module name is required")
        String name,

        @NotNull(message = "Course ID is required")
        UUID courseId,

        @NotNull(message = "Lecturer ID is required")
        UUID lecturerId
) {
}
