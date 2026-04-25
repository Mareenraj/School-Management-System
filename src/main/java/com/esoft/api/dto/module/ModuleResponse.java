package com.esoft.api.dto.module;

import java.util.UUID;

public record ModuleResponse(
        UUID id,
        String name,
        UUID courseId,
        String courseName,
        UUID lecturerId,
        String lecturerName
) {
}
