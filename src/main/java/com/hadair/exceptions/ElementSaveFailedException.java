package com.hadair.exceptions;

public class ElementSaveFailedException extends RuntimeException {
    public ElementSaveFailedException(String errorMessage) {
        super(errorMessage);
    }
}
