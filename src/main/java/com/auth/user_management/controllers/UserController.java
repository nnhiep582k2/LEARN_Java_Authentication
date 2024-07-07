package com.auth.user_management.controllers;

import com.auth.user_management.constants.AppCode;
import com.auth.user_management.dtos.ServiceResponse;
import com.auth.user_management.dtos.UserResponse;
import com.auth.user_management.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User controller")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Return a list of users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get all users successfully"),
            @ApiResponse(responseCode = "403", description = "Has no permission")
    })
    @SecurityRequirement(name = "Bearer Token")
    public ResponseEntity<ServiceResponse<UserResponse>> get() {
        try {
            List<UserResponse> users = userService.get();
            return ResponseEntity.ok(ServiceResponse.<UserResponse>builder()
                    .pageData(users)
                    .code(AppCode.Success)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ServiceResponse.<UserResponse>builder()
                    .pageData(null)
                    .code(AppCode.ServerError)
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Return a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get the user's information successfully"),
            @ApiResponse(responseCode = "403", description = "Bad credentials")
    })
    @SecurityRequirement(name = "Bearer Token")
    public ResponseEntity<ServiceResponse<UserResponse>> get(@PathVariable Integer id) {
        try {
            UserResponse user = userService.get(id);
            if (user != null) {
                return ResponseEntity.ok(ServiceResponse.<UserResponse>builder()
                        .data(user)
                        .code(AppCode.Success)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServiceResponse.<UserResponse>builder()
                        .data(null)
                        .code(AppCode.NotFound)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ServiceResponse.<UserResponse>builder()
                    .data(null)
                    .code(AppCode.ServerError)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Return success or not")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete the user's information successfully"),
            @ApiResponse(responseCode = "403", description = "Bad credentials")
    })
    @SecurityRequirement(name = "Bearer Token")
    public ResponseEntity<ServiceResponse<Boolean>> delete(@PathVariable Integer id) {
        try {
            boolean isSuccess = userService.delete(id);
            if (isSuccess) {
                return ResponseEntity.ok(ServiceResponse.<Boolean>builder()
                        .data(true)
                        .code(AppCode.Success)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServiceResponse.<Boolean>builder()
                        .data(false)
                        .code(AppCode.NotFound)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ServiceResponse.<Boolean>builder()
                    .data(false)
                    .code(AppCode.ServerError)
                    .build());
        }
    }
}
