package com.esoft.api.service;

import com.esoft.api.dto.lecturer.LecturerRequest;
import com.esoft.api.dto.lecturer.LecturerResponse;
import com.esoft.api.entity.Lecturer;
import com.esoft.api.entity.User;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.LecturerRepository;
import com.esoft.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;

    public LecturerService(LecturerRepository lecturerRepository,
                           UserRepository userRepository) {
        this.lecturerRepository = lecturerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LecturerResponse create(LecturerRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.userId()));

        if (lecturerRepository.existsByUser(user)) {
            throw new DuplicateResourceException("Lecturer profile already exists for this user");
        }

        Lecturer lecturer = Lecturer.builder()
                .user(user)
                .build();

        return toResponse(lecturerRepository.save(lecturer));
    }

    @Transactional(readOnly = true)
    public List<LecturerResponse> getAll() {
        return lecturerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LecturerResponse getById(UUID id) {
        return toResponse(findLecturerOrThrow(id));
    }

    @Transactional
    public void delete(UUID id) {
        Lecturer lecturer = findLecturerOrThrow(id);
        lecturerRepository.delete(lecturer);
    }

    private Lecturer findLecturerOrThrow(UUID id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "id", id));
    }

    private LecturerResponse toResponse(Lecturer lecturer) {
        return new LecturerResponse(
                lecturer.getId(),
                lecturer.getUser().getId(),
                lecturer.getUser().getName(),
                lecturer.getUser().getEmail()
        );
    }
}
