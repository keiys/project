package com.management.task.services.exceptions.taskexceptions;

import com.management.task.ApiException;

public class TaskApiException extends ApiException {
    public TaskApiException(String message) {
        super(message);
    }

    public TaskApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
