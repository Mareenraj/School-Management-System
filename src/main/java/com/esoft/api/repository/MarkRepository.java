package com.esoft.api.repository;

import com.esoft.api.entity.Assignment;
import com.esoft.api.entity.Mark;
import com.esoft.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarkRepository extends JpaRepository<Mark, UUID> {

    List<Mark> findByStudent(Student student);

    List<Mark> findByAssignment(Assignment assignment);

    Optional<Mark> findByStudentAndAssignment(Student student, Assignment assignment);
}
