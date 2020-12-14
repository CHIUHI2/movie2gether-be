package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

}
