package com.auth.user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticationResponse {
    @NotBlank(message = "Access token is required")
    private String accessToken;
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
