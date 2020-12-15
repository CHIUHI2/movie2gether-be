package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.MovieDetailResponse;
import com.bootcamp.movie2gether.movie.dto.MovieListingResponse;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.mapper.MovieMapper;
import com.bootcamp.movie2gether.movie.service.MovieService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/movies")
@CrossOrigin
public class MovieController {
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    private static final String MOVIE_LISTING_MODE_ON_SHOW = "onShow";
    private static final String MOVIE_LISTING_MODE_COMING = "coming";

    @GetMapping
    public List<MovieListingResponse> getMovieListing(
            @RequestParam String mode,
            @RequestParam boolean isRecommend
    ) {
        if(mode.equals(MOVIE_LISTING_MODE_ON_SHOW)) {
            return movieMapper.toResponse(movieService.findOnShowMovies(isRecommend));
        }
        else if(mode.equals(MOVIE_LISTING_MODE_COMING)) {
            return movieMapper.toResponse(movieService.findCommingSoonMovies(isRecommend));
        }

        return Collections.emptyList();
    }

    @GetMapping
    public List<MovieListingResponse> getMovieListingByGenre(
            @RequestParam String mode,
            @RequestParam String genre
    ) {
        if(mode.equals(MOVIE_LISTING_MODE_ON_SHOW)) {
            return movieMapper.toResponse(movieService.findOnShowMoviesByGenre(genre));
        }
        else if(mode.equals(MOVIE_LISTING_MODE_COMING)) {
            return movieMapper.toResponse(movieService.findCommingSoonMoviesByGenre(genre));
        }

        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    public MovieDetailResponse getMovieDetail(@PathVariable String id) throws MovieNotFoundException {
        return movieMapper.toResponse(movieService.findById(id));
    }
}
