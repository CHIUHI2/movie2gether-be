package com.bootcamp.movie2gether.movie.exception;

public class MovieNotFoundException extends Exception {
    public MovieNotFoundException() {
        super("MOVIE NOT FOUND ERROR");
    }
}
