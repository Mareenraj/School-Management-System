package com.esoft.api.controller;

import com.esoft.api.dto.assignment.AssignmentRequest;
import com.esoft.api.dto.assignment.AssignmentResponse;
import com.esoft.api.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> create(@Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(assignmentService.getById(id));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<AssignmentResponse>> getByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(assignmentService.getByModuleId(moduleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentResponse> update(@PathVariable UUID id, @Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(assignmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //get Assignments By student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByStudentId(@PathVariable UUID studentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByStudentId(studentId));
    }
}
