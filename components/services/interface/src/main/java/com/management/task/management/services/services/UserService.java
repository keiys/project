package com.management.task.management.services.services;

import com.management.task.management.services.requests.UserRequest;
import com.management.task.management.services.responses.UserResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> getAllUsers(int page, int size);

    UserResponse findUserById(UUID userId);

    UserResponse updateUser(UUID userId, UserRequest userRequest);

    void deleteUser(UUID userId);

    UserResponse updatePassword(String email, String oldPassword, String newPassword, String confirmPassword);


}
