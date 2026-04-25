package com.esoft.api.repository;

import com.esoft.api.entity.Course;
import com.esoft.api.entity.Lecturer;
import com.esoft.api.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    List<Module> findByCourse(Course course);

    List<Module> findByLecturer(Lecturer lecturer);
}
