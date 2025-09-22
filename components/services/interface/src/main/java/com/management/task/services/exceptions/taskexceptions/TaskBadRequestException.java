package com.management.task.services.exceptions.taskexceptions;

import com.management.task.BadRequestException;

public class TaskBadRequestException extends BadRequestException {
    public TaskBadRequestException(String message) {
        super(message);
    }

    public TaskBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
