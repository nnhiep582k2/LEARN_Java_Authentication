package com.auth.user_management.dtos;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    @Schema(example = "example@gmail.com", description = "Email of the user")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;
    @Schema(example = "11111111", description = "Password of the account")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password has at least 8 characters")
    private String password;
}
