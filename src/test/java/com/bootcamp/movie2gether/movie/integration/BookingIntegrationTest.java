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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
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
//                TODO: check user id as auth is not implemented yet
//                .andExpect(jsonPath("$.userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.sessionId").value(session.getId().toHexString()))
                .andExpect(jsonPath("$.seatNumber").value("A2"))
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
        Booking booking = bookingService.book(user.getId(), session.getId(), "A2");
        //when
        //then
        mockMvc.perform(
                get(String.format("/bookings?page=0&pageSize=10&userId=%s", user.getId().toHexString()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].id").value(booking.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].seatNumber").value(booking.getSeatNumber()))
                .andExpect(jsonPath("$.content.[0].userId").value(user.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.movie.id").value(movie.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.movie.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.id").value(cinema.getId().toHexString()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.name").value(cinema.getName()))
                .andExpect(jsonPath("$.content.[0].sessionDetail.cinema.seats", hasSize(cinema.getSeats().size())))
                .andExpect(jsonPath("$.content.[0].sessionDetail.bookings", hasSize(1)))
                .andExpect(jsonPath("$.content.[0].sessionDetail.startTime", startsWith(startTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS")))))
                .andExpect(jsonPath("$.content.[0].sessionDetail.endTime", startsWith(endTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS")))))
        ;
    }


}
