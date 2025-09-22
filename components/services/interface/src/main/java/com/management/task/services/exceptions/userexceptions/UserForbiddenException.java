package com.management.task.services.exceptions.userexceptions;

import com.management.task.ForbiddenException;
import jdk.jshell.execution.FailOverExecutionControlProvider;

public class UserForbiddenException extends ForbiddenException {

    public UserForbiddenException(String message) {
        super(message);
    }


}
