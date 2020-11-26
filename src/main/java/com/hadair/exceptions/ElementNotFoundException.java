package com.hadair.exceptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
