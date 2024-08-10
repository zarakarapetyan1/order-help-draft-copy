package com.platform.exceptions.userExceptions;

import com.platform.exceptions.ApiException;

public class UserApiException extends ApiException {
    public UserApiException(String message) {
        super(message);
    }
}
