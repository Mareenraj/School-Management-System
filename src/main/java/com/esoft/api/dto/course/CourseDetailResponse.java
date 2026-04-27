package com.esoft.api.dto.course;

import com.esoft.api.dto.batch.BatchSummary;
import com.esoft.api.dto.module.ModuleSummary;

import java.util.List;
import java.util.UUID;

public record CourseDetailResponse(
        UUID id,
        String name,
        String description,
        List<BatchSummary> batches,
        List<ModuleSummary> modules,
        long totalStudents
) {
}
