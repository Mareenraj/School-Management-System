package com.esoft.api.dto.batch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BatchRequest(
        @NotBlank(message = "Batch name is required")
        String name,

        @Positive(message = "Year must be a positive number")
        int year
) {
}
