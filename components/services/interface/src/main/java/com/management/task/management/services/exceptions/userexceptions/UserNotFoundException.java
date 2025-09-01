package com.management.task.management.services.exceptions.userexceptions;

import com.management.task.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
