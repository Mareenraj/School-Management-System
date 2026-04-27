package com.esoft.api.service;

import com.esoft.api.dto.batch.BatchSummary;
import com.esoft.api.dto.course.CourseDetailResponse;
import com.esoft.api.dto.course.CourseRequest;
import com.esoft.api.dto.course.CourseResponse;
import com.esoft.api.dto.module.ModuleSummary;
import com.esoft.api.dto.student.StudentResponse;
import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Course;
import com.esoft.api.entity.Module;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import com.esoft.api.repository.CourseRepository;
import com.esoft.api.repository.ModuleRepository;
import com.esoft.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository,
                         BatchRepository batchRepository,
                         ModuleRepository moduleRepository,
                         StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.batchRepository = batchRepository;
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
    }

    // ─── Course CRUD ───────────────────────────────────────────────────

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Course course = Course.builder()
                .name(request.name())
                .description(request.description())
                .build();
        return toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(UUID id) {
        return toResponse(findCourseOrThrow(id));
    }

    @Transactional
    public CourseResponse update(UUID id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        course.setName(request.name());
        course.setDescription(request.description());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(UUID id) {
        Course course = findCourseOrThrow(id);
        courseRepository.delete(course);
    }

    // ─── Hierarchical Queries ──────────────────────────────────────────

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseDetail(UUID courseId) {
        Course course = findCourseOrThrow(courseId);

        List<BatchSummary> batches = batchRepository.findByCourse(course).stream()
                .map(batch -> new BatchSummary(
                        batch.getId(),
                        batch.getName(),
                        batch.getYear(),
                        studentRepository.countByBatch(batch)
                ))
                .toList();

        List<ModuleSummary> modules = moduleRepository.findByCourse(course).stream()
                .map(module -> new ModuleSummary(
                        module.getId(),
                        module.getName(),
                        module.getLecturer().getUser().getName()
                ))
                .toList();

        long totalStudents = studentRepository.countByBatch_Course(course);

        return new CourseDetailResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                batches,
                modules,
                totalStudents
        );
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByCourseId(UUID courseId) {
        Course course = findCourseOrThrow(courseId);
        return studentRepository.findByBatch_Course(course).stream()
                .map(this::toStudentResponse)
                .toList();
    }

    // ─── Helper Methods ────────────────────────────────────────────────

    private Course findCourseOrThrow(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    private CourseResponse toResponse(Course course) {
        long batchCount = batchRepository.countByCourse(course);
        long moduleCount = moduleRepository.countByCourse(course);
        long studentCount = studentRepository.countByBatch_Course(course);

        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                batchCount,
                moduleCount,
                studentCount
        );
    }

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getName(),
                student.getUser().getEmail(),
                student.getBatch() != null ? student.getBatch().getId() : null,
                student.getBatch() != null ? student.getBatch().getName() : null
        );
    }
}
