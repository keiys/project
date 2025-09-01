package com.management.task;

public abstract class ResourceAlreadyExcistException extends RuntimeException {

    public ResourceAlreadyExcistException(String message) {
        super(message);
    }

    public ResourceAlreadyExcistException(String message, Throwable cause) {
        super(message, cause);
    }
}
