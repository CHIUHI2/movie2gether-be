package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public Booking book(String userId, String sessionId, String seatNumber) throws AlreadyBookedException {
        try {
            Booking booking = new Booking(null, userId, seatNumber, sessionId);
            return bookingRepository.save(booking);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyBookedException();
        }
    }
}
