package com.esoft.api.controller;

import com.esoft.api.dto.batch.*;
import com.esoft.api.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // ─── Batch CRUD ────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<BatchResponse> create(@Valid @RequestBody BatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(batchService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAll() {
        return ResponseEntity.ok(batchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchResponse> update(@PathVariable UUID id, @Valid @RequestBody BatchRequest request) {
        return ResponseEntity.ok(batchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Student Management within Batch ───────────────────────────────

    @GetMapping("/{batchId}/students")
    public ResponseEntity<List<BatchStudentResponse>> getStudentsByBatchId(@PathVariable UUID batchId) {
        return ResponseEntity.ok(batchService.getStudentsByBatchId(batchId));
    }

    @PostMapping("/{batchId}/students")
    public ResponseEntity<BatchStudentResponse> assignStudentToBatch(
            @PathVariable UUID batchId,
            @Valid @RequestBody BatchStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(batchService.assignStudentToBatch(batchId, request));
    }

    @PostMapping("/{batchId}/students/bulk")
    public ResponseEntity<List<BatchStudentResponse>> bulkAssignStudents(
            @PathVariable UUID batchId,
            @Valid @RequestBody BulkBatchStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(batchService.bulkAssignStudents(batchId, request));
    }

    @DeleteMapping("/{batchId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromBatch(
            @PathVariable UUID batchId,
            @PathVariable UUID studentId) {
        batchService.removeStudentFromBatch(batchId, studentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{batchId}/students/{studentId}/transfer")
    public ResponseEntity<BatchStudentResponse> transferStudent(
            @PathVariable UUID batchId,
            @PathVariable UUID studentId,
            @RequestParam UUID newBatchId) {
        return ResponseEntity.ok(batchService.transferStudent(batchId, studentId, newBatchId));
    }
}
