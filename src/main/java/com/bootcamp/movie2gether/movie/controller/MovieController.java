package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.MovieDetailResponse;
import com.bootcamp.movie2gether.movie.dto.MovieListingResponse;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.mapper.MovieMapper;
import com.bootcamp.movie2gether.movie.service.MovieService;
import com.bootcamp.movie2gether.user.dto.UserProfileResponse;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/movies")
@CrossOrigin
@ResponseBody
public class MovieController {
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MovieService movieService;

    private static final String MOVIE_LISTING_MODE_ON_SHOW = "onShow";
    private static final String MOVIE_LISTING_MODE_COMING = "coming";

    @GetMapping(params = {
            "mode",
            "isRecommend"
    })
    public List<MovieListingResponse> getMovieListing(
            @RequestParam("mode") String mode,
            @RequestParam("isRecommend") boolean isRecommend
    ) {
        if (mode.equals(MOVIE_LISTING_MODE_ON_SHOW)) {
            return movieMapper.toResponse(movieService.findOnShowMovies(isRecommend));
        } else if (mode.equals(MOVIE_LISTING_MODE_COMING)) {
            return movieMapper.toResponse(movieService.findComingSoonMovies(isRecommend));
        }

        return Collections.emptyList();
    }

    @GetMapping(params = {
            "mode",
            "genre"
    })
    public List<MovieListingResponse> getMovieListingByGenre(
            @RequestParam("mode") String mode,
            @RequestParam("genre") String genre
    ) {
        if (mode.equals(MOVIE_LISTING_MODE_ON_SHOW)) {
            return movieMapper.toResponse(movieService.findOnShowMoviesByGenre(genre));
        } else if (mode.equals(MOVIE_LISTING_MODE_COMING)) {
            return movieMapper.toResponse(movieService.findComingSoonMoviesByGenre(genre));
        }

        return Collections.emptyList();
    }

    @GetMapping(
            path = "/{id}",
            params = {
                    "userId"
            }
    )
    public MovieDetailResponse getMovieDetail(@PathVariable String id, @RequestParam("userId") String userId) throws MovieNotFoundException, UserNotFoundException {
        MovieDetailResponse movieDetailResponse = movieMapper.toResponse(movieService.findById(id));

        List<User> bookedFriends = movieService.findBookedFriends(userId, id);
        List<UserProfileResponse> bookedFriendResponses = bookedFriends.stream()
                .map((user) -> userMapper.toUserProfileResponse(user))
                .collect(Collectors.toList());

        movieDetailResponse.setBookedFriends(bookedFriendResponses);

        return movieDetailResponse;
    }
}
