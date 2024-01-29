package com.technical.exception;

public class ConflictedBookingException extends RuntimeException {
    public ConflictedBookingException(String message) {
        super(message);
    }
}
