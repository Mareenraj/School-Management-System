package com.esoft.api.controller;

import com.esoft.api.dto.auth.MessageResponse;
import com.esoft.api.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private AdminUserService adminUserService;

    @InjectMocks
    private AdminUserController adminUserController;

    @Test
    void createUser_shouldReturnCreatedWithMessage() {
        MessageResponse serviceResponse = new MessageResponse("Created");
        when(adminUserService.createUser(null)).thenReturn(serviceResponse);

        ResponseEntity<MessageResponse> response = adminUserController.createUser(null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(serviceResponse, response.getBody());
        verify(adminUserService).createUser(null);
    }
}
