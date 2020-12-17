package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import com.bootcamp.movie2gether.user.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @Autowired
    BookingService bookingService;

    public Movie findById(String id) throws MovieNotFoundException {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    public List<Movie> findComingSoonMovies(boolean isRecommend) {
        List<Movie> movies = movieRepository.findByReleaseDateGreaterThan(ZonedDateTime.now());

        if (isRecommend) {
            return movies.stream()
                    .sorted(Comparator.comparing(Movie::getPopularity, Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toList());
        }

        return movies;
    }

    public List<Movie> findOnShowMovies(boolean isRecommend) {
        List<Movie> movies = movieRepository.findByReleaseDateNotGreaterThan(ZonedDateTime.now());

        if (isRecommend) {
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

    public List<User> findBookedFriends(String userId, String movieId) throws UserNotFoundException {
        User user = userService.findById(userId);
        Set<ObjectId> friendIdSet = user.getFriends();

        List<Session> sessions = sessionService.findByMovieId(movieId);
        List<ObjectId> sessionIdList = sessions.stream().map(Session::getId).collect(Collectors.toList());

        List<Booking> bookings = bookingService.findBySessionIdList(sessionIdList);
        Set<ObjectId> bookedUserIdSet = bookings.stream().map(Booking::getUserId).collect(Collectors.toSet());

        friendIdSet.retainAll(bookedUserIdSet);

        return friendIdSet.stream().map((id) -> {
                    try {
                        return userService.findById(id.toString());
                    } catch (UserNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
