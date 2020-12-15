package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    public Movie findById(String id) throws MovieNotFoundException {
        if(!movieRepository.existsById(id)){
            throw new MovieNotFoundException();
        }
        return movieRepository.findById(id).get();
    }
}
