package com.management.task.management.services.exceptions.userexceptions;

import com.management.task.ResourceAlreadyExcistException;

public class UserAlreadyExcistException extends ResourceAlreadyExcistException {

    public UserAlreadyExcistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExcistException(String message) {
        super(message);
    }
}
