package com.technical.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiErrorResponse {

    private final HttpStatus errorCode;
    private final String message;
    private final List<String> details;

    public ApiErrorResponse(HttpStatus errorCode, String message, List<String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
