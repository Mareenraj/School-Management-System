package com.esoft.api.service;

import com.esoft.api.dto.student.StudentRequest;
import com.esoft.api.dto.student.StudentResponse;
import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Student;
import com.esoft.api.entity.User;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import com.esoft.api.repository.StudentRepository;
import com.esoft.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          BatchRepository batchRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
    }

    // ─── Student CRUD ──────────────────────────────────────────────────

    @Transactional
    public StudentResponse create(StudentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.userId()));

        if (studentRepository.existsByUser(user)) {
            throw new DuplicateResourceException("Student profile already exists for this user");
        }

        Batch batch = null;
        if (request.batchId() != null) {
            batch = batchRepository.findById(request.batchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.batchId()));
        }

        Student student = Student.builder()
                .user(user)
                .batch(batch)
                .build();

        return toResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        return toResponse(findStudentOrThrow(id));
    }

    @Transactional
    public StudentResponse update(UUID id, StudentRequest request) {
        Student student = findStudentOrThrow(id);

        if (request.batchId() != null) {
            Batch batch = batchRepository.findById(request.batchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.batchId()));
            student.setBatch(batch);
        } else {
            student.setBatch(null);
        }

        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void delete(UUID id) {
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    // ─── Specialized Queries ───────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StudentResponse> getUnassignedStudents() {
        return studentRepository.findByBatchIsNull().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByLecturerId(UUID lecturerId) {
        return studentRepository.findStudentsByLecturerId(lecturerId).stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── Helper Methods ────────────────────────────────────────────────

    private Student findStudentOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    private StudentResponse toResponse(Student student) {
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
