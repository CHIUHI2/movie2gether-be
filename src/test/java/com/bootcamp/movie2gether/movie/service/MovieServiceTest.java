package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(movieRepository.findById(anyString())).thenReturn(Optional.of(expected));
        when(movieRepository.existsById(anyString())).thenReturn(true);

        //when
        Movie movie = movieService.findById("0");

        //then
        assertEquals(expected, movie);
    }

    @Test
    public void should_throw_movie_not_found_exception_when_browse_with_a_non_existed_id_given_the_movie_detail_exists() {
        //given
        Movie expected = new Movie();

        when(movieRepository.existsById(anyString())).thenReturn(false);

        //when
        MovieNotFoundException movieNotFoundException = assertThrows(MovieNotFoundException.class, () -> movieService.findById("1"));

        //then
        assertEquals("MOVIE NOT FOUND ERROR", movieNotFoundException.getMessage());
    }

    @Test
    public void should_return_movie_when_find_comming_soon_movies_given_movies() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateNotGreaterThan(ZonedDateTime.now())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findCommingSoonMovies(false);

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_on_show_movies_given_movies() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateNotGreaterThan(ZonedDateTime.now())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findOnShowMovies(false);

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_comming_soon_movies_by_genre_given_movies_genre() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateGreaterThanAndGenres(ZonedDateTime.now(), anyString())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findCommingSoonMoviesByGenre(anyString());

        //then
        assertEquals(expectedMovies, returnedMovies);
    }

    @Test
    public void should_return_movie_when_find_on_show_movies_by_genre_given_movies_genre() {
        //given
        List<Movie> expectedMovies = Collections.singletonList(new Movie());

        when(movieRepository.findByReleaseDateNotGreaterThanAndGenres(ZonedDateTime.now(), anyString())).thenReturn(expectedMovies);

        //when
        List<Movie> returnedMovies = movieService.findOnShowMoviesByGenre(anyString());

        //then
        assertEquals(expectedMovies, returnedMovies);
    }
}
