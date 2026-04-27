package com.esoft.api.service;

import com.esoft.api.dto.batch.*;
import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Course;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import com.esoft.api.repository.CourseRepository;
import com.esoft.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public BatchService(BatchRepository batchRepository,
                        CourseRepository courseRepository,
                        StudentRepository studentRepository) {
        this.batchRepository = batchRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    // ─── Batch CRUD ────────────────────────────────────────────────────

    @Transactional
    public BatchResponse create(BatchRequest request) {
        Course course = findCourseOrThrow(request.courseId());

        Batch batch = Batch.builder()
                .name(request.name())
                .year(request.year())
                .course(course)
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
        Course course = findCourseOrThrow(request.courseId());

        batch.setName(request.name());
        batch.setYear(request.year());
        batch.setCourse(course);

        return toResponse(batchRepository.save(batch));
    }

    @Transactional
    public void delete(UUID id) {
        Batch batch = findBatchOrThrow(id);
        batchRepository.delete(batch);
    }

    // ─── Hierarchical Queries ──────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BatchResponse> getByCourseId(UUID courseId) {
        Course course = findCourseOrThrow(courseId);
        return batchRepository.findByCourse(course).stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── Student Management within Batch ───────────────────────────────

    @Transactional(readOnly = true)
    public List<BatchStudentResponse> getStudentsByBatchId(UUID batchId) {
        Batch batch = findBatchOrThrow(batchId);
        return studentRepository.findByBatch(batch).stream()
                .map(student -> toStudentResponse(student, batch))
                .toList();
    }

    @Transactional
    public BatchStudentResponse assignStudentToBatch(UUID batchId, BatchStudentRequest request) {
        Batch batch = findBatchOrThrow(batchId);
        Student student = findStudentOrThrow(request.studentId());

        if (student.getBatch() != null && student.getBatch().getId().equals(batchId)) {
            throw new DuplicateResourceException("Student is already assigned to this batch");
        }

        if (student.getBatch() != null) {
            throw new IllegalArgumentException(
                    "Student is already assigned to batch '" + student.getBatch().getName()
                            + "'. Remove them first or use the transfer endpoint.");
        }

        student.setBatch(batch);
        studentRepository.save(student);

        return toStudentResponse(student, batch);
    }

    @Transactional
    public void removeStudentFromBatch(UUID batchId, UUID studentId) {
        Batch batch = findBatchOrThrow(batchId);
        Student student = findStudentOrThrow(studentId);

        if (student.getBatch() == null || !student.getBatch().getId().equals(batchId)) {
            throw new ResourceNotFoundException("Student", "batchId", batchId);
        }

        student.setBatch(null);
        studentRepository.save(student);
    }

    @Transactional
    public List<BatchStudentResponse> bulkAssignStudents(UUID batchId, BulkBatchStudentRequest request) {
        Batch batch = findBatchOrThrow(batchId);
        List<BatchStudentResponse> results = new ArrayList<>();

        for (UUID studentId : request.studentIds()) {
            Student student = findStudentOrThrow(studentId);

            if (student.getBatch() != null) {
                throw new IllegalArgumentException(
                        "Student '" + student.getUser().getName()
                                + "' is already assigned to batch '" + student.getBatch().getName() + "'");
            }

            student.setBatch(batch);
            studentRepository.save(student);
            results.add(toStudentResponse(student, batch));
        }

        return results;
    }

    @Transactional
    public BatchStudentResponse transferStudent(UUID batchId, UUID studentId, UUID newBatchId) {
        Batch currentBatch = findBatchOrThrow(batchId);
        Student student = findStudentOrThrow(studentId);

        if (student.getBatch() == null || !student.getBatch().getId().equals(batchId)) {
            throw new ResourceNotFoundException("Student", "batchId", batchId);
        }

        Batch newBatch = findBatchOrThrow(newBatchId);
        student.setBatch(newBatch);
        studentRepository.save(student);

        return toStudentResponse(student, newBatch);
    }

    // ─── Helper Methods ────────────────────────────────────────────────

    private Batch findBatchOrThrow(UUID id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));
    }

    private Course findCourseOrThrow(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    private Student findStudentOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    private BatchResponse toResponse(Batch batch) {
        long studentCount = studentRepository.countByBatch(batch);
        return new BatchResponse(
                batch.getId(),
                batch.getName(),
                batch.getYear(),
                batch.getCourse().getId(),
                batch.getCourse().getName(),
                studentCount
        );
    }

    private BatchStudentResponse toStudentResponse(Student student, Batch batch) {
        return new BatchStudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getName(),
                student.getUser().getEmail(),
                batch.getId(),
                batch.getName()
        );
    }
}
