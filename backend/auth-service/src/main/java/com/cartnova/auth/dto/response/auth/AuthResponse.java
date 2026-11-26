package com.cartnova.auth.dto.response.auth;

import com.cartnova.auth.enums.Role;

public record AuthResponse(

        String accessToken,
        String tokenType,
        Long expiresIn,
        Long userId,
        String name,
        String email,
        Role role

) {}