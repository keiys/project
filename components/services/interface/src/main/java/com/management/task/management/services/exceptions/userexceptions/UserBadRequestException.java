package com.management.task.management.services.exceptions.userexceptions;

import com.management.task.BadRequestException;

public class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(String message) {
        super(message);
    }

    public UserBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
