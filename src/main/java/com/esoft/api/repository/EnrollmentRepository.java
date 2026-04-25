package com.esoft.api.repository;

import com.esoft.api.entity.Enrollment;
import com.esoft.api.entity.Module;
import com.esoft.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByModule(Module module);

    boolean existsByStudentAndModule(Student student, Module module);
}
