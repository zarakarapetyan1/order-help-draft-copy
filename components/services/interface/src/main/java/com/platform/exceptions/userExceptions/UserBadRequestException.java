package com.platform.exceptions.userExceptions;

import com.platform.exceptions.BadRequestException;

public class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
