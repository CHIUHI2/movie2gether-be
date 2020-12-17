package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    MovieRepository movieRepository;


    public Page<Cinema> getPagedCinemas(Integer page, Integer pageSize) {
        return cinemaRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Page<Cinema> getPagedCinemasByMovieId(ObjectId movieId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());
        List<Cinema> cinemasByMovieId = movieRepository.getCinemasByMovieId(movieId.toHexString(), pageable);
        long count = movieRepository.countCinemasByMovieId(movieId.toHexString());
        return new PageImpl<>(
                cinemasByMovieId,
                pageable,
                count);
    }
}
