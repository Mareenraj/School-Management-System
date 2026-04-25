package com.esoft.api.service;

import com.esoft.api.dto.attendance.AttendanceRequest;
import com.esoft.api.dto.attendance.AttendanceResponse;
import com.esoft.api.entity.Attendance;
import com.esoft.api.entity.Module;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.AttendanceRepository;
import com.esoft.api.repository.ModuleRepository;
import com.esoft.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             ModuleRepository moduleRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public AttendanceResponse create(AttendanceRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.studentId()));
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.moduleId()));

        Attendance attendance = Attendance.builder()
                .student(student)
                .module(module)
                .date(request.date())
                .status(request.status())
                .build();

        return toResponse(attendanceRepository.save(attendance));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return attendanceRepository.findByStudent(student).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getByModuleId(UUID moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", moduleId));
        return attendanceRepository.findByModule(module).stream().map(this::toResponse).toList();
    }

    @Transactional
    public AttendanceResponse update(UUID id, AttendanceRequest request) {
        Attendance attendance = findOrThrow(id);
        attendance.setDate(request.date());
        attendance.setStatus(request.status());
        return toResponse(attendanceRepository.save(attendance));
    }

    @Transactional
    public void delete(UUID id) {
        attendanceRepository.delete(findOrThrow(id));
    }

    private Attendance findOrThrow(UUID id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
    }

    private AttendanceResponse toResponse(Attendance a) {
        return new AttendanceResponse(a.getId(), a.getStudent().getId(),
                a.getStudent().getUser().getName(), a.getModule().getId(),
                a.getModule().getName(), a.getDate(), a.getStatus());
    }
}
