package com.esoft.api.repository;

import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Course;
import com.esoft.api.entity.Student;
import com.esoft.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByUser(User user);

    List<Student> findByBatch(Batch batch);

    boolean existsByUser(User user);

    List<Student> findByBatchIsNull();

    List<Student> findByBatch_Course(Course course);

    long countByBatch(Batch batch);

    long countByBatch_Course(Course course);
}
