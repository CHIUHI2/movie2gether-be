package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.SessionBookingDetail;
import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final CinemaRepository cinemaRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, SessionRepository sessionRepository, CinemaRepository cinemaRepository, MongoTemplate mongoTemplate) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.cinemaRepository = cinemaRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    public List<Booking> book(ObjectId userId, ObjectId sessionId, List<String> seatNumbers) throws AlreadyBookedException {
        User user = userRepository.findById(userId.toHexString()).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        Session session = sessionRepository.findById(sessionId.toHexString()).orElseThrow(() -> new IllegalArgumentException("Invalid sessionId"));
        Cinema cinema = cinemaRepository.findById(session.getCinemaId().toHexString()).orElseThrow(() -> new IllegalStateException(String.format("Session %s pointing to invalid cinema", session.getId().toHexString())));
        if (!seatNumbers.stream().allMatch(seatNumber -> cinema.getSeats().stream().anyMatch(seat -> seat.getNumber().equals(seatNumber))))
            throw new IllegalArgumentException("Invalid seatNumber(s)");
        try {
            List<Booking> list = new ArrayList<>();
            for (String seatNumber : seatNumbers) {
                Booking insert = bookingRepository.insert(
                        Booking.builder()
                                .userId(userId)
                                .seatNumber(seatNumber)
                                .sessionId(sessionId)
                                .build());
                list.add(insert);
            }
            return list;
        } catch (DuplicateKeyException exception) {
            throw new AlreadyBookedException();
        }
    }

    public Page<SessionBookingDetail> getPagedSessionBookingDetailByUserId(ObjectId userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("sessionDetail.startTime").descending());
        List<SessionBookingDetail> sessionBookingDetailsByUserId = bookingRepository.getSessionBookingDetailByUserId(userId.toHexString(), pageable);
        Long count = bookingRepository.countSessionBookingDetailByUserId(userId.toHexString());
        return new PageImpl<>(sessionBookingDetailsByUserId, pageable, count);

    }

    public List<Booking> findBySessionIdList(List<ObjectId> sessionIdList) {
        return bookingRepository.findBySessionIdList(sessionIdList);
    }
}
