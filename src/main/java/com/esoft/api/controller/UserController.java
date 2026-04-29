package com.esoft.api.controller;

import com.esoft.api.dto.auth.UserListItemResponse;
import com.esoft.api.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AdminUserService adminUserService;

    public UserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserListItemResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }
}
