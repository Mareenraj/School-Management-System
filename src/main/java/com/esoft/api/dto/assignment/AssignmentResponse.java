package com.esoft.api.dto.assignment;

import java.time.LocalDate;
import java.util.UUID;

public record AssignmentResponse(
        UUID id,
        UUID moduleId,
        String moduleName,
        String title,
        String description,
        LocalDate dueDate
) {
}
