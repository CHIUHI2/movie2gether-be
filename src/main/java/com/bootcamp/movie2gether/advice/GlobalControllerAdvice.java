package com.bootcamp.movie2gether.advice;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ErrorResponse handleIllegalArgument(IllegalArgumentException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DuplicateKeyException.class})
    public ErrorResponse handleDuplicateKey(DuplicateKeyException exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.name(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class})
    public ErrorResponse handleBadCredentialException(Exception exception) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.name(), exception.getMessage());
    }
}
