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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @BeforeEach
    void clearDB() {
        bookingRepository.deleteAll();
        sessionRepository.deleteAll();
        cinemaRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "spring")
    void should_get_paged_session_details_when_get_sessions_given_20_movie_sessions() throws Exception {
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
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").isString())
                .andExpect(jsonPath("$.[0].movie.id").value(movie.getId().toHexString()))
                .andExpect(jsonPath("$.[0].movie.title").value("Interstellar"))
                .andExpect(jsonPath("$.[0].cinema.id").value(cinema.getId().toHexString()))
                .andExpect(jsonPath("$.[0].cinema.name").value("cinema"))
                .andExpect(jsonPath("$.[0].cinema.seats", hasSize(10)))
                .andExpect(jsonPath("$.[0].bookings", hasSize(1)))
                .andExpect(jsonPath("$.[0].startTime").value(startTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"))))
                .andExpect(jsonPath("$.[0].endTime").value(endTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"))))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void should_get_filtered_sessions_when_get_sessions_with_movie_or_cinema_filter_options_given_sessions() throws Exception {
        //given
        Movie movie1 = movieRepository.save(Movie.builder().onShow(true).title("Tenet").build());
        Movie movie2 = movieRepository.save(Movie.builder().onShow(true).title("1917").build());
        Cinema cinemaA = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("AAAA")
                .build()
        );
        Cinema cinemaB = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("BBBB")
                .build()
        );
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(2));
        Session session1B = sessionRepository.save(Session.builder().cinemaId(cinemaB.getId()).movieId(movie1.getId()).startTime(startTime).endTime(endTime).build());
        Session session2A = sessionRepository.save(Session.builder().cinemaId(cinemaA.getId()).movieId(movie2.getId()).startTime(startTime).endTime(endTime).build());
        //when
        //then
        mockMvc.perform(
                get(String.format("/sessions?page=0&pageSize=2&movieId=%s&cinemaId=%s",
                        movie2.getId().toHexString(),
                        cinemaA.getId().toHexString())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(session2A.getId().toHexString()))
                .andExpect(jsonPath("$.[0].movie.id").value(movie2.getId().toHexString()))
                .andExpect(jsonPath("$.[0].movie.title").value(movie2.getTitle()))
                .andExpect(jsonPath("$.[0].cinema.id").value(cinemaA.getId().toHexString()))
                .andExpect(jsonPath("$.[0].cinema.name").value(cinemaA.getName()))
                .andExpect(jsonPath("$.[0].cinema.seats", hasSize(10)))
                .andExpect(jsonPath("$.[0].bookings", hasSize(1)))
                .andExpect(jsonPath("$.[0].startTime").value(startTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"))))
                .andExpect(jsonPath("$.[0].endTime").value(endTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"))))
        ;

    }

}
