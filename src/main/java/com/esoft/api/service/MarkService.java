package com.esoft.api.service;

import com.esoft.api.dto.mark.MarkRequest;
import com.esoft.api.dto.mark.MarkResponse;
import com.esoft.api.entity.Assignment;
import com.esoft.api.entity.Mark;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.AssignmentRepository;
import com.esoft.api.repository.MarkRepository;
import com.esoft.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final NotificationService notificationService;

    public MarkService(MarkRepository markRepository,
                       StudentRepository studentRepository,
                       AssignmentRepository assignmentRepository,
                       NotificationService notificationService) {
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public MarkResponse create(MarkRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.studentId()));
        Assignment assignment = assignmentRepository.findById(request.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", request.assignmentId()));

        Mark mark = Mark.builder()
                .student(student)
                .assignment(assignment)
                .score(request.score())
                .build();

        MarkResponse response = toResponse(markRepository.save(mark));
        notificationService.evaluateAndNotify(student);
        return response;
    }

    @Transactional(readOnly = true)
    public List<MarkResponse> getAll() {
        return markRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MarkResponse getById(UUID id) {
        return toResponse(findMarkOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<MarkResponse> getByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return markRepository.findByStudent(student).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MarkResponse> getByAssignmentId(UUID assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", assignmentId));
        return markRepository.findByAssignment(assignment).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MarkResponse update(UUID id, MarkRequest request) {
        Mark mark = findMarkOrThrow(id);
        mark.setScore(request.score());
        MarkResponse response = toResponse(markRepository.save(mark));
        notificationService.evaluateAndNotify(mark.getStudent());
        return response;
    }

    @Transactional
    public void delete(UUID id) {
        Mark mark = findMarkOrThrow(id);
        markRepository.delete(mark);
    }

    private Mark findMarkOrThrow(UUID id) {
        return markRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mark", "id", id));
    }

    private MarkResponse toResponse(Mark mark) {
        return new MarkResponse(
                mark.getId(),
                mark.getStudent().getId(),
                mark.getStudent().getUser().getName(),
                mark.getAssignment().getId(),
                mark.getAssignment().getTitle(),
                mark.getScore()
        );
    }
}
