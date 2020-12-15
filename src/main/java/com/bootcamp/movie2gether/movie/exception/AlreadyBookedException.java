package com.bootcamp.movie2gether.movie.exception;

public class AlreadyBookedException extends Exception {
    public AlreadyBookedException() {
        super("This seat has already been booked");
    }
}
