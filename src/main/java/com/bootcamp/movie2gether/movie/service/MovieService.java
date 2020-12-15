package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    public Movie findById(String id) throws MovieNotFoundException {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public List<Movie> test() {
        return this.movieRepository.findByReleaseDate(ZonedDateTime.now());
    }

    public List<Movie> findComingSoonMovies(boolean isRecommend) {
        List<Movie> movies = movieRepository.findByReleaseDateGreaterThan(ZonedDateTime.now());

        if(isRecommend) {
            return movies.stream()
                    .sorted(Comparator.comparing(Movie::getPopularity, Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toList());
        }

        return movies;
    }

    public List<Movie> findOnShowMovies(boolean isRecommend) {
        List<Movie> movies = movieRepository.findByReleaseDateNotGreaterThan(ZonedDateTime.now());

        if(isRecommend) {
            return movies.stream()
                    .sorted(Comparator.comparing(Movie::getPopularity, Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toList());
        }

        return movies;
    }

    public List<Movie> findComingSoonMoviesByGenre(String genre) {
        return movieRepository.findByReleaseDateGreaterThanAndGenres(ZonedDateTime.now(), genre);
    }

    public List<Movie> findOnShowMoviesByGenre(String genre) {
        return movieRepository.findByReleaseDateNotGreaterThanAndByGenres(ZonedDateTime.now(), genre);
    }
}
