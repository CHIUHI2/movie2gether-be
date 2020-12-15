package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.MovieDetailResponse;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.mapper.MovieMapper;
import com.bootcamp.movie2gether.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@CrossOrigin
@ResponseBody
public class MovieController {
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public MovieDetailResponse getMovieDetail(@PathVariable String id) throws MovieNotFoundException {
        return movieMapper.toResponse(movieService.findById(id));
    }
}
