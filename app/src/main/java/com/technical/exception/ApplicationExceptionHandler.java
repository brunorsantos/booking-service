package com.technical.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Collections;
import java.util.stream.Collectors;



@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Validation error", errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.singletonList("Resource not found"));
    }
}