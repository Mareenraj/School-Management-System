package com.esoft.api.repository;

import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {

    List<Batch> findByCourse(Course course);

    long countByCourse(Course course);
}
