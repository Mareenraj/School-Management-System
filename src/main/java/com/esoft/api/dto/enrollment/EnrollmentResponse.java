package com.esoft.api.dto.enrollment;

import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        UUID studentId,
        String studentName,
        UUID moduleId,
        String moduleName
) {
}
