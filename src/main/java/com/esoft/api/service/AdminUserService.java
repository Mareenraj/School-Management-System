package com.esoft.api.service;

import com.esoft.api.dto.auth.CreateUserRequest;
import com.esoft.api.dto.auth.MessageResponse;
import com.esoft.api.entity.Batch;
import com.esoft.api.entity.Lecturer;
import com.esoft.api.entity.Student;
import com.esoft.api.entity.User;
import com.esoft.api.entity.enums.Role;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import com.esoft.api.repository.LecturerRepository;
import com.esoft.api.repository.StudentRepository;
import com.esoft.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final BatchRepository batchRepository;

    public AdminUserService(UserRepository userRepository,
                            StudentRepository studentRepository,
                            LecturerRepository lecturerRepository,
                            BatchRepository batchRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.batchRepository = batchRepository;
    }

    @Transactional
    public MessageResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email is already registered: " + request.email());
        }

        if (request.role() != Role.STUDENT && request.role() != Role.LECTURER) {
            throw new IllegalArgumentException("Admin can only create STUDENT or LECTURER accounts.");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .role(request.role())
                .isVerified(false)
                .build();

        userRepository.save(user);

        if (request.role() == Role.STUDENT) {
            Batch batch = null;
            if (request.batchId() != null) {
                batch = batchRepository.findById(request.batchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.batchId()));
            }
            Student student = Student.builder()
                    .user(user)
                    .batch(batch)
                    .build();
            studentRepository.save(student);
        } else {
            Lecturer lecturer = Lecturer.builder()
                    .user(user)
                    .build();
            lecturerRepository.save(lecturer);
        }

        return new MessageResponse(request.role().name() + " account created successfully. The user can now sign up.");
    }
}
