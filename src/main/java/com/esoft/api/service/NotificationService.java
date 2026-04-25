package com.esoft.api.service;

import com.esoft.api.dto.notification.NotificationResponse;
import com.esoft.api.entity.Mark;
import com.esoft.api.entity.Notification;
import com.esoft.api.entity.Student;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.MarkRepository;
import com.esoft.api.repository.NotificationRepository;
import com.esoft.api.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private static final double GOOD_PERFORMANCE_THRESHOLD = 75.0;
    private static final double NEEDS_IMPROVEMENT_THRESHOLD = 40.0;

    private static final String GOOD_PERFORMANCE_MESSAGE = "Good Performance";
    private static final String NEEDS_IMPROVEMENT_MESSAGE = "You need to improve your self";

    private final NotificationRepository notificationRepository;
    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               MarkRepository markRepository,
                               StudentRepository studentRepository) {
        this.notificationRepository = notificationRepository;
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Evaluates a student's overall marks average and creates a notification
     * if the average crosses a performance threshold.
     * Called automatically after marks are created or updated.
     */
    @Transactional
    public void evaluateAndNotify(Student student) {
        List<Mark> marks = markRepository.findByStudent(student);

        if (marks.isEmpty()) {
            return;
        }

        double average = marks.stream()
                .mapToInt(Mark::getScore)
                .average()
                .orElse(0.0);

        String message = resolveMessage(average);

        if (message != null) {
            Notification notification = Notification.builder()
                    .student(student)
                    .message(message)
                    .averageScore(Math.round(average * 100.0) / 100.0)
                    .read(false)
                    .build();

            notificationRepository.save(notification);
            log.info("Notification created for student {}: '{}' (avg: {:.2f})",
                    student.getUser().getName(), message, average);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return notificationRepository.findByStudentOrderByCreatedAtDesc(student).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return notificationRepository.findByStudentAndReadFalseOrderByCreatedAtDesc(student).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public NotificationResponse markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    private String resolveMessage(double average) {
        if (average >= GOOD_PERFORMANCE_THRESHOLD) {
            return GOOD_PERFORMANCE_MESSAGE;
        } else if (average < NEEDS_IMPROVEMENT_THRESHOLD) {
            return NEEDS_IMPROVEMENT_MESSAGE;
        }
        return null;
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getStudent().getId(),
                n.getStudent().getUser().getName(),
                n.getMessage(),
                n.getAverageScore(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
