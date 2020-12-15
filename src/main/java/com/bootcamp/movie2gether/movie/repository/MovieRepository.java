package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByReleaseDateGreaterThan(ZonedDateTime releaseDate);
    List<Movie> findByReleaseDateNotGreaterThan(ZonedDateTime releaseDate);
    List<Movie> findByReleaseDateGreaterThanAndGenres(ZonedDateTime releaseDate, String genre);
    List<Movie> findByReleaseDateNotGreaterThanAndGenres(ZonedDateTime releaseDate, String genre);
}

