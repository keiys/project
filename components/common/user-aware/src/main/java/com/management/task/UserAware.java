package com.management.task;

import com.management.task.services.responses.UserResponse;

public interface UserAware {

    default UserResponse getCurrentUser() {
        return UserContext.getCurrentUser();
    }

    default String getCurrentUsername() {
        return UserContext.getCurrentUser().email();
    }
}
