package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.BookingDetailResponse;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
        if(!seatNumbers.stream().allMatch(seatNumber -> cinema.getSeats().stream().anyMatch(seat -> seat.getNumber().equals(seatNumber))))
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

    public Page<BookingDetailResponse> getPagedBookingDetailsByUserId(ObjectId userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AggregationOperation> aggregationOperations =
                Arrays.asList(match(Criteria.where("userId").is(userId)),
                       group("sessionId").first("$sessionId").as("sessionId"),
                        lookup("session", "sessionId", "_id", "sessionDetail"),
                        unwind("$sessionDetail"),
                        lookup("cinema", "sessionDetail.cinemaId", "_id", "sessionDetail.cinema"),
                        lookup("movies", "sessionDetail.movieId", "_id", "sessionDetail.movie"),
                        lookup("booking", "sessionDetail._id", "sessionId", "sessionDetail.bookings"),
                        unwind("$sessionDetail.cinema"),
                        unwind("$sessionDetail.movie"),
                        sort(Sort.Direction.ASC, "sessionDetail.startTime"),
                        //group(Fields.from(Fields.field("_id", "sessionId"))),
                        skip((long) pageSize * page),
                        limit(pageSize)
                );
        Aggregation dataAggregation = Aggregation.newAggregation(aggregationOperations);

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query, "booking");

        List<BookingDetailResponse> mappedResults = mongoTemplate.aggregate(dataAggregation, "booking", BookingDetailResponse.class).getMappedResults();
        return new PageImpl<>(mappedResults, pageable, count);


    }
}
