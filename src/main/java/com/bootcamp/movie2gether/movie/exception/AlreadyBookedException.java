package com.bootcamp.movie2gether.movie.exception;

public class AlreadyBookedException extends Exception {
    static final String MESSAGE = "Requested seat(s) has already been booked.";
    public AlreadyBookedException() {
        super(MESSAGE);
    }
}
