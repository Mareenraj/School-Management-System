package com.esoft.api.service;

import com.esoft.api.dto.module.ModuleRequest;
import com.esoft.api.dto.module.ModuleResponse;
import com.esoft.api.dto.student.StudentResponse;
import com.esoft.api.entity.Course;
import com.esoft.api.entity.Lecturer;
import com.esoft.api.entity.Module;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.CourseRepository;
import com.esoft.api.repository.EnrollmentRepository;
import com.esoft.api.repository.LecturerRepository;
import com.esoft.api.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final EnrollmentRepository enrollmentRepository;

    public ModuleService(ModuleRepository moduleRepository,
                         CourseRepository courseRepository,
                         LecturerRepository lecturerRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // ─── Module CRUD ───────────────────────────────────────────────────

    @Transactional
    public ModuleResponse create(ModuleRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.courseId()));
        Lecturer lecturer = lecturerRepository.findById(request.lecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", request.lecturerId()));

        Module module = Module.builder()
                .name(request.name())
                .course(course)
                .lecturer(lecturer)
                .build();

        return toResponse(moduleRepository.save(module));
    }

    @Transactional(readOnly = true)
    public List<ModuleResponse> getAll() {
        return moduleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ModuleResponse getById(UUID id) {
        return toResponse(findModuleOrThrow(id));
    }

    @Transactional
    public ModuleResponse update(UUID id, ModuleRequest request) {
        Module module = findModuleOrThrow(id);

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.courseId()));
        Lecturer lecturer = lecturerRepository.findById(request.lecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", request.lecturerId()));

        module.setName(request.name());
        module.setCourse(course);
        module.setLecturer(lecturer);

        return toResponse(moduleRepository.save(module));
    }

    @Transactional
    public void delete(UUID id) {
        Module module = findModuleOrThrow(id);
        moduleRepository.delete(module);
    }

    // ─── Hierarchical Queries ──────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ModuleResponse> getByCourseId(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        return moduleRepository.findByCourse(course).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ModuleResponse> getByLecturerId(UUID lecturerId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", lecturerId));
        return moduleRepository.findByLecturer(lecturer).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByModuleId(UUID moduleId) {
        Module module = findModuleOrThrow(moduleId);
        return enrollmentRepository.findByModule(module).stream()
                .map(enrollment -> {
                    var student = enrollment.getStudent();
                    return new StudentResponse(
                            student.getId(),
                            student.getUser().getId(),
                            student.getUser().getName(),
                            student.getUser().getEmail(),
                            student.getBatch() != null ? student.getBatch().getId() : null,
                            student.getBatch() != null ? student.getBatch().getName() : null
                    );
                })
                .toList();
    }

    // ─── Helper Methods ────────────────────────────────────────────────

    private Module findModuleOrThrow(UUID id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
    }

    private ModuleResponse toResponse(Module module) {
        return new ModuleResponse(
                module.getId(),
                module.getName(),
                module.getCourse().getId(),
                module.getCourse().getName(),
                module.getLecturer().getId(),
                module.getLecturer().getUser().getName()
        );
    }
}
