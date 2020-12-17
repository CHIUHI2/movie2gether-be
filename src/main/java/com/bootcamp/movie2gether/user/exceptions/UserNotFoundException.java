package com.bootcamp.movie2gether.user.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found.");
    }
}
