package com.auth.user_management.mappers;

import com.auth.user_management.dtos.UserResponse;
import com.auth.user_management.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseMapper implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        if (user != null) {
            return new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole());
        }
        return null;
    }
}
