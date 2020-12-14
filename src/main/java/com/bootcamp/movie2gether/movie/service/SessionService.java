package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<SessionDetail> getDetailsPaged(Integer page, Integer pageSize) {
        Aggregation aggregation = Aggregation.newAggregation(
                lookup("cinema", "cinemaId", "_id", "cinema"),
                lookup("movie", "movieId", "_id", "movie"),
                lookup("booking", "_id", "sessionId", "bookings"),
                unwind("movie"),
                unwind("cinema"),
                sort(Sort.Direction.ASC, "startTime"),
                skip((long) pageSize * page),
                limit(pageSize)
        );
        return mongoTemplate.aggregate(aggregation, "session", SessionDetail.class).getMappedResults();
    }


}
