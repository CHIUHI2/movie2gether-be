package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    MovieService movieService;

    @Test
    public void should_return_movie_detail_when_browse_with_a_valid_id_given_the_movie_detail_exists() throws Exception {
        //given
        Movie expected = new Movie();

        when(movieRepository.findById("0")).thenReturn(Optional.of(expected));

        //when
        Movie movie = movieService.findById("0");

        //then
        assertEquals(expected, movie);
    }

    @Test
    public void should_return_movie_when_find_coming_soon_movies_given_movies() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateGreaterThan(any())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findComingSoonMovies(false);

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_on_show_movies_given_movies() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateNotGreaterThan(any())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findOnShowMovies(false);

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_coming_soon_movies_by_genre_given_movies_genre() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateGreaterThanAndGenres(any(), anyString())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findComingSoonMoviesByGenre("action");

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_on_show_movies_by_genre_given_movies_genre() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateNotGreaterThanAndByGenres(any(), anyString())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findOnShowMoviesByGenre("action");

        //then
        assertEquals(expectedMovies, returnedMovies);
    }
}
