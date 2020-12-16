package com.bootcamp.movie2gether.user.exceptions;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super("Password strength is not enough");
    }
}
