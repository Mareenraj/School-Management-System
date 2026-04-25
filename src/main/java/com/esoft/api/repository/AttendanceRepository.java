package com.esoft.api.repository;

import com.esoft.api.entity.Attendance;
import com.esoft.api.entity.Module;
import com.esoft.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    List<Attendance> findByStudent(Student student);

    List<Attendance> findByModule(Module module);

    List<Attendance> findByStudentAndModule(Student student, Module module);
}
