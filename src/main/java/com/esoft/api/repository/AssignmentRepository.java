package com.esoft.api.repository;

import com.esoft.api.entity.Assignment;
import com.esoft.api.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByModule(Module module);

    @Query("SELECT a FROM Assignment a JOIN Enrollment e ON a.module = e.module WHERE e.student.id = :studentId")
    List<Assignment> findAssignmentsByStudentId(@Param("studentId") UUID studentId);
}
