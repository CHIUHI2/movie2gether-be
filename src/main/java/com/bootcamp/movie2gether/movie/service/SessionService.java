package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<SessionDetail> getDetailsPaged(Integer page, Integer pageSize, ObjectId cinemaId, ObjectId movieId) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        Optional.ofNullable(cinemaId).ifPresent(objectId -> aggregationOperations.add(match(Criteria.where("cinemaId").is(objectId))));
        Optional.ofNullable(movieId).ifPresent(objectId -> aggregationOperations.add(match(Criteria.where("movieId").is(objectId))));
        aggregationOperations.addAll(
                List.of(
                        lookup("cinema", "cinemaId", "_id", "cinema"),
                        lookup("movie", "movieId", "_id", "movie"),
                        lookup("booking", "_id", "sessionId", "bookings"),
                        unwind("movie"),
                        unwind("cinema"),
                        sort(Sort.Direction.ASC, "startTime"),
                        skip((long) pageSize * page),
                        limit(pageSize)
                )
        );
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, "session", SessionDetail.class).getMappedResults();
    }


}
