package com.esoft.api.dto.lecturer;

import java.util.UUID;

public record LecturerResponse(
        UUID id,
        UUID userId,
        String userName,
        String userEmail
) {
}
