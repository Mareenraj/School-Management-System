package com.esoft.api.dto.batch;

import java.util.UUID;

public record BatchSummary(
        UUID id,
        String name,
        int year,
        long studentCount
) {
}
