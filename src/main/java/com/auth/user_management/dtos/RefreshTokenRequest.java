package com.auth.user_management.dtos;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {
    @Schema(example = "example@gmail.com", description = "Email of the user")
    @NotBlank(message = "Email token is required")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
