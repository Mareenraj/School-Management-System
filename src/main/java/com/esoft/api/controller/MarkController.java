package com.esoft.api.controller;

import com.esoft.api.dto.mark.MarkRequest;
import com.esoft.api.dto.mark.MarkResponse;
import com.esoft.api.service.MarkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping
    public ResponseEntity<MarkResponse> create(@Valid @RequestBody MarkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(markService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MarkResponse>> getAll() {
        return ResponseEntity.ok(markService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(markService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarkResponse>> getByStudentId(@PathVariable UUID studentId) {
        return ResponseEntity.ok(markService.getByStudentId(studentId));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<MarkResponse>> getByAssignmentId(@PathVariable UUID assignmentId) {
        return ResponseEntity.ok(markService.getByAssignmentId(assignmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarkResponse> update(@PathVariable UUID id, @Valid @RequestBody MarkRequest request) {
        return ResponseEntity.ok(markService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        markService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
