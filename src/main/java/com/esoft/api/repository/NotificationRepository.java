package com.esoft.api.repository;

import com.esoft.api.entity.Notification;
import com.esoft.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByStudentOrderByCreatedAtDesc(Student student);

    List<Notification> findByStudentAndReadFalseOrderByCreatedAtDesc(Student student);
}
