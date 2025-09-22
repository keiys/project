package com.management.task.services.exceptions.taskexceptions;

import com.management.task.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
