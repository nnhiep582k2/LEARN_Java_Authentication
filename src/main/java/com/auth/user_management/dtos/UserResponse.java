package com.auth.user_management.dtos;

import com.auth.user_management.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
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
