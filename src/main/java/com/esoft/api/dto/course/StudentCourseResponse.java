package com.esoft.api.dto.course;

import java.util.UUID;

public record StudentCourseResponse(
        UUID id,
        String name,
        String description
) {
}
