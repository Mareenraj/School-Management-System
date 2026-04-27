package com.esoft.api.controller;

import com.esoft.api.dto.batch.BatchResponse;
import com.esoft.api.dto.course.CourseDetailResponse;
import com.esoft.api.dto.course.CourseRequest;
import com.esoft.api.dto.course.CourseResponse;
import com.esoft.api.dto.module.ModuleResponse;
import com.esoft.api.dto.student.StudentResponse;
import com.esoft.api.service.BatchService;
import com.esoft.api.service.CourseService;
import com.esoft.api.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final BatchService batchService;
    private final ModuleService moduleService;

    public CourseController(CourseService courseService,
                            BatchService batchService,
                            ModuleService moduleService) {
        this.courseService = courseService;
        this.batchService = batchService;
        this.moduleService = moduleService;
    }

    // ─── Course CRUD ───────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable UUID id, @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Hierarchical Sub-Resource Endpoints ───────────────────────────

    @GetMapping("/{courseId}/detail")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseDetail(courseId));
    }

    @GetMapping("/{courseId}/batches")
    public ResponseEntity<List<BatchResponse>> getBatchesByCourseId(@PathVariable UUID courseId) {
        return ResponseEntity.ok(batchService.getByCourseId(courseId));
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<ModuleResponse>> getModulesByCourseId(@PathVariable UUID courseId) {
        return ResponseEntity.ok(moduleService.getByCourseId(courseId));
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourseId(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getStudentsByCourseId(courseId));
    }
}
