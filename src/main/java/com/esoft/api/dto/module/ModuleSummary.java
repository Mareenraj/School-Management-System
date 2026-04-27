package com.esoft.api.dto.module;

import java.util.UUID;

public record ModuleSummary(
        UUID id,
        String name,
        String lecturerName
) {
}
