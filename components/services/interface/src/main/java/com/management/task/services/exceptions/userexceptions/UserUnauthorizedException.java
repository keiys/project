package com.management.task.services.exceptions.userexceptions;

import com.management.task.UnauthorizedException;

public class UserUnauthorizedException extends UnauthorizedException {


    public UserUnauthorizedException(String message) {
        super(message);
    }
}
