package com.cartnova.auth.service.impl;

import com.cartnova.auth.dto.request.auth.LoginRequest;
import com.cartnova.auth.dto.request.auth.RegisterRequest;
import com.cartnova.auth.dto.response.auth.AuthResponse;
import com.cartnova.auth.entity.User;
import com.cartnova.auth.enums.Role;
import com.cartnova.auth.exception.EmailAlreadyExistsException;
import com.cartnova.auth.exception.InvalidCredentialsException;
import com.cartnova.auth.mapper.UserMapper;
import com.cartnova.auth.repository.UserRepository;
import com.cartnova.auth.security.JwtService;
import com.cartnova.auth.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserMapper userMapper) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(savedUser.getEmail())
                        .password(savedUser.getPassword())
                        .authorities("ROLE_" + savedUser.getRole().name())
                        .build();

        String token = jwtService.generateToken(userDetails);

        return userMapper.toAuthResponse(
                savedUser,
                token,
                jwtService.getExpiration() / 1000
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = request.email().trim().toLowerCase();

        try {

            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.password()
                    )
            );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new InvalidCredentialsException(
                                    "Invalid email or password"
                            ));

            String token = jwtService.generateToken(userDetails);

            return userMapper.toAuthResponse(
                    user,
                    token,
                    jwtService.getExpiration() / 1000
            );

        } catch (org.springframework.security.core.AuthenticationException ex) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }
    }
}
