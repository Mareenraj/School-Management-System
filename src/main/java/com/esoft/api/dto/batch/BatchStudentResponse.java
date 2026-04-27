package com.esoft.api.dto.batch;

import java.util.UUID;

public record BatchStudentResponse(
        UUID studentId,
        UUID userId,
        String studentName,
        String studentEmail,
        UUID batchId,
        String batchName
) {
}
