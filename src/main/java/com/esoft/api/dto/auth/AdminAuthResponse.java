package com.esoft.api.dto.auth;

import com.esoft.api.entity.enums.Role;

import java.util.UUID;

public record AdminAuthResponse(
        UUID adminId,
        String accessToken,
        String refreshToken,
        String email,
        String name,
        Role role
) {
}
