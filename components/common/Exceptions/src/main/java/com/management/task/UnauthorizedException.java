package com.management.task;

public abstract class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }

}
