package com.esoft.api.controller;

import com.esoft.api.dto.lecturer.LecturerRequest;
import com.esoft.api.dto.lecturer.LecturerResponse;
import com.esoft.api.dto.module.ModuleResponse;
import com.esoft.api.dto.student.StudentResponse;
import com.esoft.api.service.LecturerService;
import com.esoft.api.service.ModuleService;
import com.esoft.api.service.StudentService;
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
    private final ModuleService moduleService;
    private final StudentService studentService;

    public LecturerController(LecturerService lecturerService,
                              ModuleService moduleService,
                              StudentService studentService) {
        this.lecturerService = lecturerService;
        this.moduleService = moduleService;
        this.studentService = studentService;
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

    //Update letcurer
    @PutMapping("/{id}")
    public ResponseEntity<LecturerResponse> update(@PathVariable UUID id, @Valid @RequestBody LecturerRequest request) {
        return ResponseEntity.ok(lecturerService.update(id, request));
    }

    //GetModulesByLectureId
    @GetMapping("/{id}/modules")
    public ResponseEntity<List<ModuleResponse>> getModulesByLecturerId(@PathVariable UUID id) {
        return ResponseEntity.ok(moduleService.getByLecturerId(id));
    }

    //Get Students by lectureID
    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByLecturerId(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentsByLecturerId(id));
    }

}
