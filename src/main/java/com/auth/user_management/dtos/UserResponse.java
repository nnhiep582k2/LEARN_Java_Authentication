package com.auth.user_management.dtos;

import com.auth.user_management.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private Integer id;
    @NotBlank(message = "First name is required")
    private String firstname;
    private String lastname;
    @Schema(example = "example@gmail.com", description = "Email of the user")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserResponse(Integer id, String firstname, String lastname, String email, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }
}
