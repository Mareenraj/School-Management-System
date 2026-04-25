package com.esoft.api.dto.course;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        String name,
        String description
) {
}
