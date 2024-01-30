package com.technical.exception;

public class ConflictedDateException extends RuntimeException {
    public ConflictedDateException(String message) {
        super(message);
    }
}
