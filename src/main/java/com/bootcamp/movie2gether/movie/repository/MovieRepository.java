package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
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
    @Query("{$and:[{'releaseDate' : {$lte : ?0}}, { 'genres': {$eq : ?1 }}]}")
    List<Movie> findByReleaseDateNotGreaterThanAndByGenres(ZonedDateTime releaseDate, String genre);

    @Aggregation(
            pipeline = {
                    "{$match: {_id: ObjectId(?0)}}",
                    "{$lookup: {from: 'session',localField: '_id',foreignField: 'movieId',as: 'session'}}",
                    "{$unwind: {path: '$session'}}",
                    "{$lookup: {from: 'cinema',localField: 'session.cinemaId',foreignField: '_id',as: 'cinema'}}",
                    "{$unwind: {path: '$cinema'}}",
                    "{$group: {_id: {cinema:\"$cinema._id\"},cinema: {$first: '$cinema'}}}",
                    "{$replaceRoot: {newRoot: '$cinema'}}"
            })
    List<Cinema> getCinemasByMovieId(String movieId, Pageable pageable);

    @Aggregation(pipeline = {
            "{$match: {_id: ObjectId(?0)}}",
            "{$lookup: {from: 'session',localField: '_id',foreignField: 'movieId',as: 'session'}}",
            "{$unwind: {path: '$session'}}",
            "{$lookup: {from: 'cinema',localField: 'session.cinemaId',foreignField: '_id',as: 'cinema'}}",
            "{$unwind: {path: '$cinema'}}",
            "{$group: {_id: {cinema:\"$cinema._id\"},cinema: {$first: '$cinema'}}}",
            "{$count: 'count'}"
    })
    Long countCinemasByMovieId(String movieId);
}

