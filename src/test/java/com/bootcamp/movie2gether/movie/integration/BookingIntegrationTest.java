package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.dto.BookingRequest;
import com.bootcamp.movie2gether.movie.entity.*;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.movie.service.BookingService;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
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
                                objectMapper.writeValueAsString(BookingRequest.builder()
                                        .seatNumbers(Collections.singletonList("A2"))
                                        .sessionId(session.getId().toHexString())
                                        .userId(user.getId().toHexString())
                                        .build())
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[0].id").isString())
                .andExpect(jsonPath("$.[0].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[0].userId").isString())
                .andExpect(jsonPath("$.[0].sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.[0].seatNumber").value("A2"))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void should_return_booking_info_when_book_with_session_and_multiple_seats_given_available_seat() throws Exception {
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
                                objectMapper.writeValueAsString(BookingRequest.builder()
                                        .seatNumbers(Arrays.asList("A2", "A3", "A4"))
                                        .sessionId(session.getId().toHexString())
                                        .userId(user.getId().toHexString())
                                        .build())
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[0].id").isString())
                .andExpect(jsonPath("$.[0].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[0].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[0].sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.[0].seatNumber").value("A2"))
                .andExpect(jsonPath("$.[1].id").isString())
                .andExpect(jsonPath("$.[1].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[1].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[1].sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.[1].seatNumber").value("A3"))
                .andExpect(jsonPath("$.[2].id").isString())
                .andExpect(jsonPath("$.[2].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[2].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.[2].sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.[2].seatNumber").value("A4"))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void should_return_booking_with_session_detail_when_get_booking_with_userId() throws Exception {
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
        List<Booking> bookings = bookingService.book(user.getId(), session.getId(), Collections.singletonList("A2"));
        //when
        //then
        mockMvc.perform(
                get(String.format("/bookings?page=0&pageSize=10&userId=%s", user.getId().toHexString()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id").value(bookings.get(0).getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].seatNumber").value(bookings.get(0).getSeatNumber()))
                .andExpect(jsonPath("$.content.[0].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.movie.id").value(movie.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.movie.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.id").value(cinema.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.name").value(cinema.getName()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.seats", hasSize(cinema.getSeats().size())))
                .andExpect(jsonPath("$.content.[0].sessionDetail.bookings", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].sessionDetail.startTime", startsWith(startTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.content.[0].sessionDetail.endTime", startsWith(endTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")))))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void should_return_400_when_book_with_no_seats() throws Exception {
        //given
        User user = userRepository.save(new User());
        Cinema cinema = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("AAAA")
                .build()
        );
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());
        //when
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(BookingRequest.builder()
                                        .seatNumbers(null)
                                        .sessionId(session.getId().toHexString())
                                        .userId(user.getId().toHexString())
                                        .build())
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid BookingRequest"));
    }

    @Test
    @WithMockUser(value = "spring")
    void should_return_400_when_book_with_invalid_user_id() throws Exception {
        //given
        Cinema cinema = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("AAAA")
                .build()
        );
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());
        //when
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(BookingRequest.builder()
                                        .seatNumbers(Collections.singletonList("A1"))
                                        .sessionId(session.getId().toHexString())
                                        .userId(new ObjectId().toHexString())
                                        .build())
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid userId"));
    }

    @Test
    @WithMockUser(value = "spring")
    void should_return_400_when_book_with_invalid_seats() throws Exception {
        //given
        User user = userRepository.save(new User());
        Cinema cinema = cinemaRepository.save(Cinema.builder().seats(IntStream.range(0, 10)
                .mapToObj(i -> new Seat(String.format("A%d", i)))
                .collect(Collectors.toList()))
                .name("AAAA")
                .build()
        );
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());
        //when
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(BookingRequest.builder()
                                        .seatNumbers(Collections.singletonList("B1"))
                                        .sessionId(session.getId().toHexString())
                                        .userId(user.getId().toHexString())
                                        .build())
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid seatNumber(s)"));
    }


}
