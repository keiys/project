package com.management.task.services.responses;

import com.management.task.services.Enums.Role;
import com.management.task.services.Enums.Status;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String name,
        String surname,
        String email,
        String password,
        String verifyCode,
        Status status,
        Role role
) {
}
