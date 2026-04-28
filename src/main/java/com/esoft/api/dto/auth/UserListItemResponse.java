package com.esoft.api.dto.auth;

import java.util.UUID;

public record UserListItemResponse(
        UUID userId,
        String name
) {
}