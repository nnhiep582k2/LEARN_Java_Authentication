package com.auth.user_management.services;

import com.auth.user_management.dtos.*;
import com.auth.user_management.mappers.UserResponseMapper;
import com.auth.user_management.models.User;
import com.auth.user_management.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserResponseMapper userResponseMapper;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return userResponseMapper.apply(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return jwtService.generateToken(user);
    }

    public String refreshToken(RefreshTokenRequest request) {
        return jwtService.refreshToken(request);
    }

    public void logout(String authHeader, String email) {
        jwtService.logout(authHeader, email);
    }
}
