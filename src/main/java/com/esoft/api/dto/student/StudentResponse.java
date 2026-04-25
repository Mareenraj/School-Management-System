package com.esoft.api.dto.student;

import java.util.UUID;

public record StudentResponse(
        UUID id,
        UUID userId,
        String userName,
        String userEmail,
        UUID batchId,
        String batchName
) {
}
