package com.esoft.api.dto.batch;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BulkBatchStudentRequest(
        @NotEmpty(message = "Student IDs list cannot be empty")
        List<@NotNull UUID> studentIds
) {
}
