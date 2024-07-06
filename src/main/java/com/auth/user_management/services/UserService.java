package com.auth.user_management.services;

import com.auth.user_management.dtos.UserResponse;
import com.auth.user_management.mappers.UserResponseMapper;
import com.auth.user_management.models.User;
import com.auth.user_management.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public List<UserResponse> get() {
        return userRepository.findAll().stream().map(userResponseMapper).toList();
    }

    public UserResponse get(Integer id) {
        return userResponseMapper.apply(userRepository.findById(id).orElse(null));
    }

    public boolean delete(Integer id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if(user == null) {
                return false;
            } else {
                userRepository.delete(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
