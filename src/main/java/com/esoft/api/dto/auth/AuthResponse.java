package com.esoft.api.dto.auth;

import com.esoft.api.entity.enums.Role;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String email,
        String name,
        Role role
) {
}
