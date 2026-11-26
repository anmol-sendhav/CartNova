package com.cartnova.auth.mapper;

import com.cartnova.auth.dto.response.auth.AuthResponse;
import com.cartnova.auth.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthResponse toAuthResponse(User user, String accessToken, long expiresIn) {
        return new AuthResponse(
                accessToken,
                "Bearer",
                expiresIn,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
