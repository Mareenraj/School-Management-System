package com.esoft.api.repository;

import com.esoft.api.entity.Lecturer;
import com.esoft.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {

    Optional<Lecturer> findByUser(User user);

    boolean existsByUser(User user);
}
