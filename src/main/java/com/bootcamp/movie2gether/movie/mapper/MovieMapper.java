package com.bootcamp.movie2gether.movie.mapper;

import com.bootcamp.movie2gether.movie.dto.MovieDetailResponse;
import com.bootcamp.movie2gether.movie.dto.MovieListingResponse;
import com.bootcamp.movie2gether.movie.entity.Movie;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    public MovieDetailResponse toResponse(Movie movie) {
        MovieDetailResponse movieDetailResponse = new MovieDetailResponse();

        BeanUtils.copyProperties(movie, movieDetailResponse);
        movieDetailResponse.setId(movie.getId().toString());

        return movieDetailResponse;
    }

    public MovieListingResponse toMovingListingResponse(Movie movie) {
        MovieListingResponse movieListingResponse = new MovieListingResponse();

        BeanUtils.copyProperties(movie, movieListingResponse);
        movieListingResponse.setId(movie.getId().toString());

        return movieListingResponse;
    }

    public List<MovieListingResponse> toResponse(List<Movie> movies) {
        return movies.stream()
                .map(this::toMovingListingResponse)
                .collect(Collectors.toList());
    }
}
