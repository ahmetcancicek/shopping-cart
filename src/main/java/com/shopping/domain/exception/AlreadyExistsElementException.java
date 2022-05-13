package com.shopping.domain.exception;

public class AlreadyExistsElementException extends RuntimeException {
    public AlreadyExistsElementException(String message) {
        super(message);
    }
}
