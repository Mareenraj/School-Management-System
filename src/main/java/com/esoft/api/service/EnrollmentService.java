package com.esoft.api.service;

import com.esoft.api.dto.enrollment.EnrollmentRequest;
import com.esoft.api.dto.enrollment.EnrollmentResponse;
import com.esoft.api.entity.Enrollment;
import com.esoft.api.entity.Module;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.EnrollmentRepository;
import com.esoft.api.repository.ModuleRepository;
import com.esoft.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             ModuleRepository moduleRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.studentId()));
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.moduleId()));

        if (enrollmentRepository.existsByStudentAndModule(student, module)) {
            throw new DuplicateResourceException("Student is already enrolled in this module");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student).module(module).build();
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return enrollmentRepository.findByStudent(student).stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(UUID id) {
        enrollmentRepository.delete(findOrThrow(id));
    }

    private Enrollment findOrThrow(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
    }

    private EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentResponse(e.getId(), e.getStudent().getId(),
                e.getStudent().getUser().getName(), e.getModule().getId(), e.getModule().getName());
    }
}
