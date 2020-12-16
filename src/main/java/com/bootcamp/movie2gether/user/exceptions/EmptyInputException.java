package com.bootcamp.movie2gether.user.exceptions;

public class EmptyInputException extends RuntimeException {
    public EmptyInputException() {
        super("Input cannot be empty");
    }
}
