package com.platform.exceptions.userExceptions;

import com.platform.exceptions.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }


}
