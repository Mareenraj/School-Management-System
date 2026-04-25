package com.esoft.api.controller;

import com.esoft.api.dto.attendance.AttendanceRequest;
import com.esoft.api.dto.attendance.AttendanceResponse;
import com.esoft.api.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> create(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAll() {
        return ResponseEntity.ok(attendanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(attendanceService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getByStudentId(@PathVariable UUID studentId) {
        return ResponseEntity.ok(attendanceService.getByStudentId(studentId));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<AttendanceResponse>> getByModuleId(@PathVariable UUID moduleId) {
        return ResponseEntity.ok(attendanceService.getByModuleId(moduleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> update(@PathVariable UUID id, @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
