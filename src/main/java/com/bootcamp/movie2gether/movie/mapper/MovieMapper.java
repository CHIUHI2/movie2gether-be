package com.bootcamp.movie2gether.movie.mapper;

import com.bootcamp.movie2gether.movie.dto.MovieDetailResponse;
import com.bootcamp.movie2gether.movie.entity.Movie;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    public MovieDetailResponse toResponse(Movie movie) {
        MovieDetailResponse movieDetailResponse = new MovieDetailResponse();
        BeanUtils.copyProperties(movie, movieDetailResponse);
        return movieDetailResponse;
    }
}
