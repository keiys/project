package com.management.task.services.services;

import com.management.task.services.requests.UserRequest;
import com.management.task.services.responses.UserResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> getAllUsers(int page, int size);

    UserResponse findUserById(UUID userId);

    UserResponse updateUser(UUID userId, UserRequest userRequest);

    void deleteUser(UUID userId);

    UserResponse updatePassword(String email, String oldPassword, String newPassword, String confirmPassword);


}
