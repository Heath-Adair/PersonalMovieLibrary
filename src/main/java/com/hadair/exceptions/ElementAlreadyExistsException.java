package com.hadair.exceptions;

public class ElementAlreadyExistsException extends RuntimeException {
    public ElementAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
