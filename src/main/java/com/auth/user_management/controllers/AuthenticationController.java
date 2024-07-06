package com.auth.user_management.controllers;

import com.auth.user_management.dtos.AuthenticationRequest;
import com.auth.user_management.dtos.AuthenticationResponse;
import com.auth.user_management.dtos.RegisterRequest;
import com.auth.user_management.dtos.UserResponse;
import com.auth.user_management.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PermitAll
    @Operation(summary = "Register", description = "Return a new account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Register successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "Login", description = "Return jwt token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "403", description = "Bad credentials")
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
