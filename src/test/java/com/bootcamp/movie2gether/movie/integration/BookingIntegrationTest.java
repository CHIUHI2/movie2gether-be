package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.entity.Seat;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.movie.service.BookingService;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingIntegrationTest {
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

    @Test
    void should_get_paged_sessions_when_get_sessions_given_10_movie_sessions() throws Exception {
        //given
        Movie movie = movieRepository.save(Movie.builder().onShow(true).title("Interstellar").build());
        Cinema cinema = cinemaRepository.save(
                Cinema.builder()
                        .seats(IntStream.range(0, 10)
                                .mapToObj(i -> new Seat(String.format("A%d", i)))
                                .collect(Collectors.toList()))
                        .name("cinema")
                        .build()
        );
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(2));
        List<Session> sessions =
                sessionRepository.saveAll(
                        IntStream.range(0, 20)
                                .mapToObj(i -> new Session(null, cinema.getId(), movie.getId(), startTime, endTime))
                                .collect(Collectors.toList())
                );

        User user = userRepository.save(new User());
        sessions.forEach(session -> {
            try {
                bookingService.book(user.getId(), session.getId(), "A5");
            } catch (AlreadyBookedException ignored) {
            }
        });


        //when
        //then
        mockMvc.perform(get("/sessions?page=0&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").isString())
                .andExpect(jsonPath("$.content[0].movieId").value(movie.getId()))
                .andExpect(jsonPath("$.content[0].movieName").value("Interstellar"))
                .andExpect(jsonPath("$.content[0].cinemaId").value(cinema.getId()))
                .andExpect(jsonPath("$.content[0].cinemaName").value("cinema"))
                .andExpect(jsonPath("$.content[0].totalSeats").value(20))
                .andExpect(jsonPath("$.content[0].availableSeats").value(19))
                .andExpect(jsonPath("$.content[0].startTime").value(startTime.toString()))
                .andExpect(jsonPath("$.content[0].endTime").value(endTime.toString()))
                .andExpect(jsonPath("$.content", hasSize(2)))
        ;
    }
}
