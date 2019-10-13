package com.hadair.exceptions;

public class ElementAlreadyExistsException extends Exception {
    public ElementAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
