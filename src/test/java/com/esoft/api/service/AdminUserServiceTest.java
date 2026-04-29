package com.esoft.api.service;

import com.esoft.api.dto.auth.CreateUserRequest;
import com.esoft.api.dto.auth.MessageResponse;
import com.esoft.api.dto.auth.UserListItemResponse;
import com.esoft.api.entity.User;
import com.esoft.api.entity.enums.Role;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.BatchRepository;
import com.esoft.api.repository.LecturerRepository;
import com.esoft.api.repository.StudentRepository;
import com.esoft.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private BatchRepository batchRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void getAllUsers_shouldReturnOnlyUserIdAndName() {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();

        User first = User.builder().id(firstId).name("Alice").email("alice@example.com").role(Role.STUDENT).build();
        User second = User.builder().id(secondId).name("Bob").email("bob@example.com").role(Role.LECTURER).build();

        when(userRepository.findAll()).thenReturn(List.of(first, second));

        List<UserListItemResponse> result = adminUserService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(firstId, result.getFirst().userId());
        assertEquals("Alice", result.getFirst().name());
        assertEquals(secondId, result.get(1).userId());
        assertEquals("Bob", result.get(1).name());
    }

    @Test
    void createUser_shouldCreateStudent_whenRoleIsStudent() {
        CreateUserRequest request = new CreateUserRequest("Student One", "student1@example.com", Role.STUDENT, null);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponse response = adminUserService.createUser(request);

        assertEquals("STUDENT account created successfully. The user can now sign up.", response.message());
        verify(studentRepository, times(1)).save(any());
        verify(lecturerRepository, never()).save(any());
    }

    @Test
    void createUser_shouldCreateLecturer_whenRoleIsLecturer() {
        CreateUserRequest request = new CreateUserRequest("Lecturer One", "lecturer1@example.com", Role.LECTURER, null);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponse response = adminUserService.createUser(request);

        assertEquals("LECTURER account created successfully. The user can now sign up.", response.message());
        verify(lecturerRepository, times(1)).save(any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest("Duplicate", "duplicate@example.com", Role.STUDENT, null);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> adminUserService.createUser(request));
    }

    @Test
    void createUser_shouldThrowIllegalArgumentException_whenRoleIsAdmin() {
        CreateUserRequest request = new CreateUserRequest("Admin", "admin@example.com", Role.ADMIN, null);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> adminUserService.createUser(request));
    }

    @Test
    void createUser_shouldThrowResourceNotFoundException_whenStudentBatchNotFound() {
        UUID batchId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("Student Two", "student2@example.com", Role.STUDENT, batchId);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminUserService.createUser(request));
    }
}
