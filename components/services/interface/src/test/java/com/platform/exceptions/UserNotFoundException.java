package com.platform.exceptions;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
