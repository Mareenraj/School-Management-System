package com.esoft.api.controller;

import com.esoft.api.dto.auth.MessageResponse;
import com.esoft.api.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerSmokeTest {

    @Test
    void authController_shouldHandleAllEndpoints() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);
        Authentication authentication = mock(Authentication.class);

        when(authService.signup(null)).thenReturn(new MessageResponse("ok"));
        when(authService.verifyOtp(null)).thenReturn(new MessageResponse("ok"));
        when(authService.resendOtp(null)).thenReturn(new MessageResponse("ok"));
        when(authService.signin(null)).thenReturn(null);
        when(authService.refreshToken(null)).thenReturn(null);
        when(authentication.getName()).thenReturn("user@example.com");
        when(authService.logout("user@example.com")).thenReturn(new MessageResponse("ok"));
        when(authService.getProfile(authentication)).thenReturn(null);

        assertEquals(HttpStatus.CREATED, controller.signup(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.verifyOtp(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.resendOtp(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.signin(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.refreshToken(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.logout(authentication).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getProfile(authentication).getStatusCode());

        verify(authService).signup(null);
        verify(authService).verifyOtp(null);
        verify(authService).resendOtp(null);
        verify(authService).signin(null);
        verify(authService).refreshToken(null);
        verify(authService).logout("user@example.com");
        verify(authService).getProfile(authentication);
    }

    @Test
    void assignmentController_shouldHandleAllEndpoints() {
        AssignmentService assignmentService = mock(AssignmentService.class);
        AssignmentController controller = new AssignmentController(assignmentService);
        UUID id = UUID.randomUUID();
        UUID moduleId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        when(assignmentService.create(null)).thenReturn(null);
        when(assignmentService.getAll()).thenReturn(List.of());
        when(assignmentService.getById(id)).thenReturn(null);
        when(assignmentService.getByModuleId(moduleId)).thenReturn(List.of());
        when(assignmentService.update(id, null)).thenReturn(null);
        when(assignmentService.getAssignmentsByStudentId(studentId)).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByModuleId(moduleId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAssignmentsByStudentId(studentId).getStatusCode());

        verify(assignmentService).create(null);
        verify(assignmentService).getAll();
        verify(assignmentService).getById(id);
        verify(assignmentService).getByModuleId(moduleId);
        verify(assignmentService).update(id, null);
        verify(assignmentService).delete(id);
        verify(assignmentService).getAssignmentsByStudentId(studentId);
    }

    @Test
    void attendanceController_shouldHandleAllEndpoints() {
        AttendanceService attendanceService = mock(AttendanceService.class);
        AttendanceController controller = new AttendanceController(attendanceService);
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID moduleId = UUID.randomUUID();

        when(attendanceService.create(null)).thenReturn(null);
        when(attendanceService.getAll()).thenReturn(List.of());
        when(attendanceService.getById(id)).thenReturn(null);
        when(attendanceService.getByStudentId(studentId)).thenReturn(List.of());
        when(attendanceService.getByModuleId(moduleId)).thenReturn(List.of());
        when(attendanceService.update(id, null)).thenReturn(null);

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByStudentId(studentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByModuleId(moduleId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());

        verify(attendanceService).create(null);
        verify(attendanceService).getAll();
        verify(attendanceService).getById(id);
        verify(attendanceService).getByStudentId(studentId);
        verify(attendanceService).getByModuleId(moduleId);
        verify(attendanceService).update(id, null);
        verify(attendanceService).delete(id);
    }

    @Test
    void batchController_shouldHandleAllEndpoints() {
        BatchService batchService = mock(BatchService.class);
        BatchController controller = new BatchController(batchService);
        UUID id = UUID.randomUUID();
        UUID batchId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID newBatchId = UUID.randomUUID();

        when(batchService.create(null)).thenReturn(null);
        when(batchService.getAll()).thenReturn(List.of());
        when(batchService.getById(id)).thenReturn(null);
        when(batchService.update(id, null)).thenReturn(null);
        when(batchService.getStudentsByBatchId(batchId)).thenReturn(List.of());
        when(batchService.assignStudentToBatch(batchId, null)).thenReturn(null);
        when(batchService.bulkAssignStudents(batchId, null)).thenReturn(List.of());
        when(batchService.transferStudent(batchId, studentId, newBatchId)).thenReturn(null);

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getStudentsByBatchId(batchId).getStatusCode());
        assertEquals(HttpStatus.CREATED, controller.assignStudentToBatch(batchId, null).getStatusCode());
        assertEquals(HttpStatus.CREATED, controller.bulkAssignStudents(batchId, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.removeStudentFromBatch(batchId, studentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.transferStudent(batchId, studentId, newBatchId).getStatusCode());

        verify(batchService).create(null);
        verify(batchService).getAll();
        verify(batchService).getById(id);
        verify(batchService).update(id, null);
        verify(batchService).delete(id);
        verify(batchService).getStudentsByBatchId(batchId);
        verify(batchService).assignStudentToBatch(batchId, null);
        verify(batchService).bulkAssignStudents(batchId, null);
        verify(batchService).removeStudentFromBatch(batchId, studentId);
        verify(batchService).transferStudent(batchId, studentId, newBatchId);
    }

    @Test
    void courseController_shouldHandleAllEndpoints() {
        CourseService courseService = mock(CourseService.class);
        BatchService batchService = mock(BatchService.class);
        ModuleService moduleService = mock(ModuleService.class);
        CourseController controller = new CourseController(courseService, batchService, moduleService);
        UUID id = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        when(courseService.create(null)).thenReturn(null);
        when(courseService.getAll()).thenReturn(List.of());
        when(courseService.getById(id)).thenReturn(null);
        when(courseService.update(id, null)).thenReturn(null);
        when(courseService.getCourseDetail(courseId)).thenReturn(null);
        when(batchService.getByCourseId(courseId)).thenReturn(List.of());
        when(moduleService.getByCourseId(courseId)).thenReturn(List.of());
        when(courseService.getStudentsByCourseId(courseId)).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getCourseDetail(courseId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getBatchesByCourseId(courseId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getModulesByCourseId(courseId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getStudentsByCourseId(courseId).getStatusCode());

        verify(courseService).create(null);
        verify(courseService).getAll();
        verify(courseService).getById(id);
        verify(courseService).update(id, null);
        verify(courseService).delete(id);
        verify(courseService).getCourseDetail(courseId);
        verify(batchService).getByCourseId(courseId);
        verify(moduleService).getByCourseId(courseId);
        verify(courseService).getStudentsByCourseId(courseId);
    }

    @Test
    void enrollmentController_shouldHandleAllEndpoints() {
        EnrollmentService enrollmentService = mock(EnrollmentService.class);
        EnrollmentController controller = new EnrollmentController(enrollmentService);
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        when(enrollmentService.create(null)).thenReturn(null);
        when(enrollmentService.getAll()).thenReturn(List.of());
        when(enrollmentService.getById(id)).thenReturn(null);
        when(enrollmentService.getByStudentId(studentId)).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByStudentId(studentId).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());

        verify(enrollmentService).create(null);
        verify(enrollmentService).getAll();
        verify(enrollmentService).getById(id);
        verify(enrollmentService).getByStudentId(studentId);
        verify(enrollmentService).delete(id);
    }

    @Test
    void lecturerController_shouldHandleAllEndpoints() {
        LecturerService lecturerService = mock(LecturerService.class);
        ModuleService moduleService = mock(ModuleService.class);
        StudentService studentService = mock(StudentService.class);
        LecturerController controller = new LecturerController(lecturerService, moduleService, studentService);
        UUID id = UUID.randomUUID();

        when(lecturerService.create(null)).thenReturn(null);
        when(lecturerService.getAll()).thenReturn(List.of());
        when(lecturerService.getById(id)).thenReturn(null);
        when(lecturerService.update(id, null)).thenReturn(null);
        when(moduleService.getByLecturerId(id)).thenReturn(List.of());
        when(studentService.getStudentsByLecturerId(id)).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getModulesByLecturerId(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getStudentsByLecturerId(id).getStatusCode());

        verify(lecturerService).create(null);
        verify(lecturerService).getAll();
        verify(lecturerService).getById(id);
        verify(lecturerService).delete(id);
        verify(lecturerService).update(id, null);
        verify(moduleService).getByLecturerId(id);
        verify(studentService).getStudentsByLecturerId(id);
    }

    @Test
    void markController_shouldHandleAllEndpoints() {
        MarkService markService = mock(MarkService.class);
        MarkController controller = new MarkController(markService);
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        when(markService.create(null)).thenReturn(null);
        when(markService.getAll()).thenReturn(List.of());
        when(markService.getById(id)).thenReturn(null);
        when(markService.getByStudentId(studentId)).thenReturn(List.of());
        when(markService.getByAssignmentId(assignmentId)).thenReturn(List.of());
        when(markService.update(id, null)).thenReturn(null);

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByStudentId(studentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getByAssignmentId(assignmentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());

        verify(markService).create(null);
        verify(markService).getAll();
        verify(markService).getById(id);
        verify(markService).getByStudentId(studentId);
        verify(markService).getByAssignmentId(assignmentId);
        verify(markService).update(id, null);
        verify(markService).delete(id);
    }

    @Test
    void moduleController_shouldHandleAllEndpoints() {
        ModuleService moduleService = mock(ModuleService.class);
        ModuleController controller = new ModuleController(moduleService);
        UUID id = UUID.randomUUID();
        UUID moduleId = UUID.randomUUID();

        when(moduleService.create(null)).thenReturn(null);
        when(moduleService.getAll()).thenReturn(List.of());
        when(moduleService.getById(id)).thenReturn(null);
        when(moduleService.update(id, null)).thenReturn(null);
        when(moduleService.getStudentsByModuleId(moduleId)).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getStudentsByModuleId(moduleId).getStatusCode());

        verify(moduleService).create(null);
        verify(moduleService).getAll();
        verify(moduleService).getById(id);
        verify(moduleService).update(id, null);
        verify(moduleService).delete(id);
        verify(moduleService).getStudentsByModuleId(moduleId);
    }

    @Test
    void notificationController_shouldHandleAllEndpoints() {
        NotificationService notificationService = mock(NotificationService.class);
        NotificationController controller = new NotificationController(notificationService);
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        when(notificationService.getByStudentId(studentId)).thenReturn(List.of());
        when(notificationService.getUnreadByStudentId(studentId)).thenReturn(List.of());
        when(notificationService.markAsRead(id)).thenReturn(null);

        assertEquals(HttpStatus.OK, controller.getByStudentId(studentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getUnreadByStudentId(studentId).getStatusCode());
        assertEquals(HttpStatus.OK, controller.markAsRead(id).getStatusCode());

        verify(notificationService).getByStudentId(studentId);
        verify(notificationService).getUnreadByStudentId(studentId);
        verify(notificationService).markAsRead(id);
    }

    @Test
    void studentController_shouldHandleAllEndpoints() {
        StudentService studentService = mock(StudentService.class);
        StudentController controller = new StudentController(studentService);
        UUID id = UUID.randomUUID();

        when(studentService.create(null)).thenReturn(null);
        when(studentService.getAll()).thenReturn(List.of());
        when(studentService.getById(id)).thenReturn(null);
        when(studentService.update(id, null)).thenReturn(null);
        when(studentService.getUnassignedStudents()).thenReturn(List.of());

        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(HttpStatus.OK, controller.getById(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.update(id, null).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(id).getStatusCode());
        assertEquals(HttpStatus.OK, controller.getUnassignedStudents().getStatusCode());

        verify(studentService).create(null);
        verify(studentService).getAll();
        verify(studentService).getById(id);
        verify(studentService).update(id, null);
        verify(studentService).delete(id);
        verify(studentService).getUnassignedStudents();
    }
}
