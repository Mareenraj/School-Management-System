package com.esoft.api.service;

import com.esoft.api.dto.batch.BatchRequest;
import com.esoft.api.dto.batch.BatchResponse;
import com.esoft.api.entity.Batch;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Transactional
    public BatchResponse create(BatchRequest request) {
        Batch batch = Batch.builder()
                .name(request.name())
                .year(request.year())
                .build();
        return toResponse(batchRepository.save(batch));
    }

    @Transactional(readOnly = true)
    public List<BatchResponse> getAll() {
        return batchRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BatchResponse getById(UUID id) {
        return toResponse(findBatchOrThrow(id));
    }

    @Transactional
    public BatchResponse update(UUID id, BatchRequest request) {
        Batch batch = findBatchOrThrow(id);
        batch.setName(request.name());
        batch.setYear(request.year());
        return toResponse(batchRepository.save(batch));
    }

    @Transactional
    public void delete(UUID id) {
        Batch batch = findBatchOrThrow(id);
        batchRepository.delete(batch);
    }

    private Batch findBatchOrThrow(UUID id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));
    }

    private BatchResponse toResponse(Batch batch) {
        return new BatchResponse(batch.getId(), batch.getName(), batch.getYear());
    }
}
