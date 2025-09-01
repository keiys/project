package com.management.task.management.services.exceptions.userexceptions;

import com.management.task.ApiException;

public class UserApiException extends ApiException {

    public UserApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserApiException(String message) {
        super(message);
    }
}
