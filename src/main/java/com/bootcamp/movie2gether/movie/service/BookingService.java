package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.BookingDetailResponse;
import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
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

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoClient client;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public List<Booking> book(ObjectId userId, ObjectId sessionId, List<String> seatNumbers) throws AlreadyBookedException {
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
                        lookup("session", "sessionId", "_id", "sessionDetail"),
                        unwind("$sessionDetail"),
                        lookup("cinema", "sessionDetail.cinemaId", "_id", "sessionDetail.cinema"),
                        lookup("movies", "sessionDetail.movieId", "_id", "sessionDetail.movie"),
                        lookup("booking", "sessionDetail._id", "sessionId", "sessionDetail.bookings"),
                        unwind("$sessionDetail.cinema"),
                        unwind("$sessionDetail.movie"),
                        sort(Sort.Direction.ASC, "sessionDetail.startTime"),
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
