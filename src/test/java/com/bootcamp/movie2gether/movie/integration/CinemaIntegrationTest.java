package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.entity.Seat;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.movie.service.BookingService;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CinemaIntegrationTest {
    public static final String BOOKINGS_PATH = "/cinemas";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    BookingService bookingService;

    @BeforeEach
    void clearDB() {
        bookingRepository.deleteAll();
        sessionRepository.deleteAll();
        cinemaRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_return_paged_unique_cinemas_when_get_cinema() throws Exception {
        //given
        Cinema cinema1 = cinemaRepository.insert(
                Cinema.builder()
                        .seats(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> new Seat(String.format("A%d", i)))
                                        .collect(Collectors.toList()))
                        .name("1111")
                        .build()
        );
        Cinema cinema2 = cinemaRepository.insert(
                Cinema.builder()
                        .seats(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> new Seat(String.format("A%d", i)))
                                        .collect(Collectors.toList()))
                        .name("2222")
                        .build()
        );
        //when
        mockMvc.perform(get(String.format("%s?page=%d&pageSize=%d", BOOKINGS_PATH, 1, 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(cinema2.getId().toHexString()))
                .andExpect(jsonPath("$.content[0].name").value(cinema2.getName()))
                .andExpect(jsonPath("$.content[0].seats", hasSize(10)))
                .andExpect(jsonPath("$.totalElements").value(2))
        ;
        //then
    }

    @Test
    void should_return_paged_unique_cinemas_when_get_cinema_by_movie_given_movie() throws Exception {
        //given
        Cinema cinema1 = cinemaRepository.insert(
                Cinema.builder()
                        .seats(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> new Seat(String.format("A%d", i)))
                                        .collect(Collectors.toList()))
                        .name("1111")
                        .build()
        );
        Cinema cinema2 = cinemaRepository.insert(
                Cinema.builder()
                        .seats(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> new Seat(String.format("A%d", i)))
                                        .collect(Collectors.toList()))
                        .name("2222")
                        .build()
        );
        Movie movie = movieRepository.insert(Movie.builder().title("mmm").build());
        sessionRepository.insert(Session.builder().cinemaId(cinema1.getId()).movieId(movie.getId()).build());
        //when
        mockMvc.perform(get(String.format("%s?page=%d&pageSize=%d&movieId=%s", BOOKINGS_PATH, 0, 1, movie.getId().toHexString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(cinema1.getId().toHexString()))
                .andExpect(jsonPath("$.content[0].name").value(cinema1.getName()))
                .andExpect(jsonPath("$.content[0].seats", hasSize(10)))
                .andExpect(jsonPath("$.totalElements").value(1))
        ;
        //then
    }
}
