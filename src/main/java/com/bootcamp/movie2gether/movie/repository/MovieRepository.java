package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByReleaseDate(ZonedDateTime releaseDate);
    List<Movie> findByReleaseDateGreaterThan(ZonedDateTime releaseDate);
    @Query("{'releaseDate' : {$lte : ?0}}")
    List<Movie> findByReleaseDateNotGreaterThan(ZonedDateTime releaseDate);
    List<Movie> findByReleaseDateGreaterThanAndGenres(ZonedDateTime releaseDate, String genre);
    @Query("{$and:[{'releaseDate' : {$lte : ?0}}, { 'genres': { $in : [?1] }}]}")
    List<Movie> findByReleaseDateNotGreaterThanAndByGenres(ZonedDateTime releaseDate, String genre);
}

