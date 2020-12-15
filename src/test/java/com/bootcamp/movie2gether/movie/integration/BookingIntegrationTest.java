package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.dto.BookingRequest;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void should_return_booking_info_when_book_with_session_and_seat_given_available_seat() throws Exception {
        //given
        Movie movie = movieRepository.save(Movie.builder().onShow(true).title("Tenet").build());
        Cinema cinema = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("AAAA")
                .build()
        );
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(2));
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).movieId(movie.getId()).startTime(startTime).endTime(endTime).build());
        User user = userRepository.save(new User("spring", "spring@mail.com", "lmao"));
        //when
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(BookingRequest.builder().seatNumber("A2").sessionId(session.getId().toHexString()).build())
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
//                .andExpect(jsonPath("$.userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.seatNumber").value("A2"))
        ;

    }


}
