package com.platform.exceptions;

public abstract class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
