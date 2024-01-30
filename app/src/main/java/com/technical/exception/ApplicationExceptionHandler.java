package com.technical.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Collections;
import java.util.stream.Collectors;



@RestControllerAdvice
@Slf4j
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
        log.warn("Resource not found", ex);
        return new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.singletonList("Resource not found"));
    }

    @ExceptionHandler(InvalidBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidBookingException(InvalidBookingException ex) {
        log.warn("Invalid booking", ex);
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.singletonList("Invalid booking"));
    }

    @ExceptionHandler(ConflictedBookingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleConflictedBookingException(ConflictedBookingException ex) {
        log.warn("Conflicted booking", ex);
        return new ApiErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), Collections.singletonList("Conflicted booking"));
    }

}
