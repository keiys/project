package com.management.task;

import com.management.task.services.responses.UserResponse;

public class UserContext {

    private static final ThreadLocal<UserResponse> currentUser = new ThreadLocal<>();

    public static UserResponse getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(UserResponse user) {
        currentUser.set(user);
    }
}
