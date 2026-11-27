package com.cartnova.auth.service;

import com.cartnova.auth.dto.request.auth.LoginRequest;
import com.cartnova.auth.dto.request.auth.RegisterRequest;
import com.cartnova.auth.dto.response.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
