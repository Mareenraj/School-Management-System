package com.esoft.api.dto.mark;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MarkRequest(
        @NotNull(message = "Student ID is required")
        UUID studentId,

        @NotNull(message = "Assignment ID is required")
        UUID assignmentId,

        @Min(value = 0, message = "Score must be at least 0")
        @Max(value = 100, message = "Score must be at most 100")
        int score
) {
}
