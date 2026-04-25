package com.esoft.api.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank(message = "Course name is required")
        String name,

        String description
) {
}
