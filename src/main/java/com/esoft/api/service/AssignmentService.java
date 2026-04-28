package com.esoft.api.service;

import com.esoft.api.dto.assignment.AssignmentRequest;
import com.esoft.api.dto.assignment.AssignmentResponse;
import com.esoft.api.entity.Assignment;
import com.esoft.api.entity.Module;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.AssignmentRepository;
import com.esoft.api.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ModuleRepository moduleRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             ModuleRepository moduleRepository) {
        this.assignmentRepository = assignmentRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public AssignmentResponse create(AssignmentRequest request) {
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.moduleId()));

        Assignment assignment = Assignment.builder()
                .module(module)
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .build();

        return toResponse(assignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAll() {
        return assignmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getById(UUID id) {
        return toResponse(findAssignmentOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getByModuleId(UUID moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", moduleId));
        return assignmentRepository.findByModule(module).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByStudentId(UUID studentId) {
        return assignmentRepository.findAssignmentsByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AssignmentResponse update(UUID id, AssignmentRequest request) {
        Assignment assignment = findAssignmentOrThrow(id);

        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.moduleId()));

        assignment.setModule(module);
        assignment.setTitle(request.title());
        assignment.setDescription(request.description());
        assignment.setDueDate(request.dueDate());

        return toResponse(assignmentRepository.save(assignment));
    }

    @Transactional
    public void delete(UUID id) {
        Assignment assignment = findAssignmentOrThrow(id);
        assignmentRepository.delete(assignment);
    }

    private Assignment findAssignmentOrThrow(UUID id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", id));
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getModule().getId(),
                assignment.getModule().getName(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate()
        );
    }
}
