package com.platform.exceptions;

public class AddressNotFoundException extends ResourceNotFoundException {
    public AddressNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
