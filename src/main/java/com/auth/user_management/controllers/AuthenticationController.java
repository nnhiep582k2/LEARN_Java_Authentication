package com.auth.user_management.controllers;

import com.auth.user_management.constants.AppCode;
import com.auth.user_management.dtos.*;
import com.auth.user_management.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ServiceResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        UserResponse userResponse = authenticationService.register(request);
        return ResponseEntity.ok(ServiceResponse.<UserResponse>builder()
                .data(userResponse)
                .code(AppCode.Success)
                .build());
    }

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "Login", description = "Return jwt token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "403", description = "Bad credentials")
    })
    public ResponseEntity<ServiceResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok(ServiceResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .code(AppCode.Success)
                .build());
    }

    @PostMapping("/refresh")
    @PermitAll
    @Operation(summary = "Refresh token", description = "Generate a new access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Refresh successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<ServiceResponse<String>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String newRefreshToken = authenticationService.refreshToken(request);
        if (newRefreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ServiceResponse.<String>builder()
                    .data(null)
                    .code(AppCode.ServerError)
                    .build());
        } else {
            return ResponseEntity.ok(ServiceResponse.<String>builder()
                    .data(newRefreshToken)
                    .code(AppCode.Success)
                    .build());
        }
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "Logout", description = "Remove access and refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<ServiceResponse<Boolean>> logout(@RequestHeader("Authorization") String authHeader,
                                                           @RequestBody String email) {
        try {
            authenticationService.logout(authHeader, email);
            return ResponseEntity.ok(ServiceResponse.<Boolean>builder()
                    .data(true)
                    .code(AppCode.Success)
                    .build());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ServiceResponse.<Boolean>builder()
                    .data(false)
                    .code(AppCode.ServerError)
                    .build());
        }
    }
}
