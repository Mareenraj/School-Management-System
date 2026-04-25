package com.esoft.api.dto.batch;

import java.util.UUID;

public record BatchResponse(
        UUID id,
        String name,
        int year
) {
}
