package com.platform;

import com.platform.exceptions.ForbiddenException;

public class SecurityException extends ForbiddenException {

    public SecurityException() {
        super("Insufficient permission");
    }
}
