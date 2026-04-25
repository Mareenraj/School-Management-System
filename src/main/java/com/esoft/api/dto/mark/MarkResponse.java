package com.esoft.api.dto.mark;

import java.util.UUID;

public record MarkResponse(
        UUID id,
        UUID studentId,
        String studentName,
        UUID assignmentId,
        String assignmentTitle,
        int score
) {
}
