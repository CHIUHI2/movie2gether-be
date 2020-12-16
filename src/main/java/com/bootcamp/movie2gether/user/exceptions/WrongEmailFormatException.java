package com.bootcamp.movie2gether.user.exceptions;

public class WrongEmailFormatException extends RuntimeException {
    public WrongEmailFormatException() {
        super("Email with invalid format provided");
    }
}
