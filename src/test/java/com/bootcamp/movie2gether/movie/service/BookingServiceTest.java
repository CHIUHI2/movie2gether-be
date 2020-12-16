package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Seat;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookingServiceTest {
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

    @BeforeEach
    void clearDB() {
        bookingRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void should_store_and_return_booking_when_book_ticket_given_session_seat_and_user() throws AlreadyBookedException {
        //given
        BookingService bookingService = new BookingService(bookingRepository, userRepository, sessionRepository, cinemaRepository, mongoTemplate);
        Seat seat = new Seat("A1");
        Cinema cinema = cinemaRepository.insert(Cinema.builder().name("cinema").seats(Collections.singletonList(seat)).build());
        User user = userRepository.save(new User());
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());

        //when
        Booking actual = bookingService.book(user.getId(), session.getId(), Collections.singletonList("A1")).get(0);

        //expect
        List<Booking> storedBookings = bookingRepository.findAll();
        assertEquals(1, storedBookings.size());
        assertEquals(storedBookings.get(0), actual);
        assertEquals(session.getId(), storedBookings.get(0).getSessionId());
        assertEquals(user.getId(), storedBookings.get(0).getUserId());
        assertEquals(seat.getNumber(), storedBookings.get(0).getSeatNumber());

    }

    @Test
    void should_throw_exception_and_do_nothing_when_book_ticket_given_session_seat_and_user_already_booked() throws AlreadyBookedException {
        //given
        BookingService bookingService = new BookingService(bookingRepository, userRepository, sessionRepository, cinemaRepository, mongoTemplate);
        Seat seat = new Seat("A1");
        Cinema cinema = cinemaRepository.insert(Cinema.builder().name("cinema").seats(Collections.singletonList(seat)).build());
        User user = userRepository.save(new User());
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());
        User userBooked = userRepository.save(new User());

        //when
        bookingService.book(userBooked.getId(), session.getId(), Collections.singletonList("A1"));
        //expect
        assertThrows(AlreadyBookedException.class, () -> bookingService.book(user.getId(), session.getId(), Collections.singletonList("A1")));
        assertEquals(1, bookingRepository.count());
    }

    @Test
    void should_no_new_booking_created_when_book_seats_given_some_seats_already_booked() throws AlreadyBookedException {
        //given
        BookingService bookingService = new BookingService(bookingRepository, userRepository, sessionRepository, cinemaRepository, mongoTemplate);
        Cinema cinema = cinemaRepository.insert(Cinema.builder().name("cinema").seats(Arrays.asList(new Seat("A1"), new Seat("A2"))).build());
        User user = userRepository.save(new User());
        Session session = sessionRepository.save(Session.builder().cinemaId(cinema.getId()).build());
        User userBooked = userRepository.save(new User());

        //when
        bookingService.book(userBooked.getId(), session.getId(), Collections.singletonList("A1"));
        //expect
        assertThrows(AlreadyBookedException.class, () -> bookingService.book(user.getId(), session.getId(), Arrays.asList("A1", "A2")));
        assertEquals(1, bookingRepository.count());
    }
}
