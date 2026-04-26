package com.esoft.api.dto.profile;

import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String name,
        String email
) {
}
