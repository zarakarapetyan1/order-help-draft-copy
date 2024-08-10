package com.platform.exceptions.userExceptions;

import com.platform.exceptions.ResourceAlreadyExistsException;

public class UserAlreadyExistException extends ResourceAlreadyExistsException {
    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
