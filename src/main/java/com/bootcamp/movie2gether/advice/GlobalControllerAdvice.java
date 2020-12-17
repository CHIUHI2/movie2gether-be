package com.bootcamp.movie2gether.advice;

import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.user.exceptions.EmptyInputException;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.exceptions.WeakPasswordException;
import com.bootcamp.movie2gether.user.exceptions.WrongEmailFormatException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MovieNotFoundException.class})
    public ErrorResponse handleMovieNotFoundException(MovieNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({AlreadyBookedException.class})
    public ErrorResponse handleBookDuplication(AlreadyBookedException exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.name(), exception.getMessage());
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class})
    public ErrorResponse handleBadCredentialException(Exception exception) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.name(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            EmptyInputException.class,
            WrongEmailFormatException.class,
            WeakPasswordException.class
    })
    public ErrorResponse handleInputExceptions(Exception exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public ErrorResponse FriendNotFoundException(UserNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(), exception.getMessage());
    }
}
