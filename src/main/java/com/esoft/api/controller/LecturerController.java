package com.esoft.api.controller;

import com.esoft.api.dto.lecturer.LecturerRequest;
import com.esoft.api.dto.lecturer.LecturerResponse;
import com.esoft.api.service.LecturerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @PostMapping
    public ResponseEntity<LecturerResponse> create(@Valid @RequestBody LecturerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LecturerResponse>> getAll() {
        return ResponseEntity.ok(lecturerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(lecturerService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        lecturerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
