package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.dto.BookingDetail;
import com.bootcamp.movie2gether.movie.entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findBySessionId(String SessionId);

    @Aggregation(pipeline ={
            "{$match: {userId: ObjectId(?0)}}",
            "{$lookup: {from: 'session',localField: 'sessionId',foreignField: '_id',as: 'sessionDetail'}}",
            "{$addFields: {sessionDetail: {$arrayElemAt: [\"$sessionDetail\", 0]}}}",
            "{$lookup: {from: 'cinema',localField: 'sessionDetail.cinemaId',foreignField: '_id',as: 'sessionDetail.cinema'}}",
            "{$addFields: {\"sessionDetail.cinema\": {$arrayElemAt: [\"$sessionDetail.cinema\", 0]}}}",
            "{$lookup: {from: 'movies',localField: 'sessionDetail.movieId',foreignField: '_id',as: 'sessionDetail.movie'}}",
            "{$addFields: {\"sessionDetail.movie\": {$arrayElemAt: [\"$sessionDetail.movie\", 0]}}}",
            "{$lookup: {from: 'booking',localField: 'sessionId',foreignField: 'sessionId',as: 'sessionDetail.bookings'}}",
    })
    List<BookingDetail> getBookingDetailsByUserId(String userId, Pageable pageable);

    @Aggregation(pipeline = {
            "{$match: {userId: ObjectId(?0)}}",
            "{$count: 'count'}",
    })
    Long countBookingDetailsByUserId(String userId);
}
