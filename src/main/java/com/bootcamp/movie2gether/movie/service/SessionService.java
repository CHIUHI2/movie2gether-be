package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<SessionDetail> getDetailsPaged(Integer page, Integer pageSize, ObjectId cinemaId, ObjectId movieId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AggregationOperation> matchOperations = new ArrayList<>();
        Optional.ofNullable(cinemaId).ifPresent(objectId -> matchOperations.add(match(Criteria.where("cinemaId").is(objectId))));
        Optional.ofNullable(movieId).ifPresent(objectId -> matchOperations.add(match(Criteria.where("movieId").is(objectId))));

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.addAll(matchOperations);
        aggregationOperations.addAll(
                Arrays.asList(
                        lookup("cinema", "cinemaId", "_id", "cinema"),
                        lookup("movies", "movieId", "_id", "movie"),
                        lookup("booking", "_id", "sessionId", "bookings"),
                        unwind("$movie"),
                        unwind("$cinema"),
                        sort(Sort.Direction.ASC, "startTime"),
                        skip((long) pageSize * page),
                        limit(pageSize)
                )

        );
        Aggregation dataAggregation = Aggregation.newAggregation(aggregationOperations);

        List<AggregationOperation> countingAggregationOperations = new ArrayList<>(matchOperations);
        countingAggregationOperations.add(group("_id").count().as("count"));
        countingAggregationOperations.add(project().andExclude("_id"));
        Aggregation countingAggregation = Aggregation.newAggregation(countingAggregationOperations);

        Query query = new Query();
        if (cinemaId != null)
            query.addCriteria(Criteria.where("cinemaId").is(cinemaId));
        if (movieId != null)
            query.addCriteria(Criteria.where("movieId").is(movieId));
        long count = mongoTemplate.count(query, "session");

        List<SessionDetail> mappedResults = mongoTemplate.aggregate(dataAggregation, "session", SessionDetail.class).getMappedResults();
        return new PageImpl<>(mappedResults, pageable, count);

    }


    public SessionDetail getSessionById(ObjectId objectId) {
        Optional<Session> sessionRes = sessionRepository.findById(objectId.toHexString());
        if (sessionRes.isPresent()) {
            Session session = sessionRes.get();
            Optional<Cinema> optionalCinema = cinemaRepository.findById(session.getCinemaId().toHexString());
            if (!optionalCinema.isPresent())
                throw new NoSuchElementException();
            Optional<Movie> optionalMovie = movieRepository.findById(session.getMovieId().toHexString());
            if (!optionalMovie.isPresent())
                throw new NoSuchElementException();

            return SessionDetail.builder().movie(optionalMovie.get()).cinema(optionalCinema.get())
                    .endTime(session.getEndTime()).startTime(session.getStartTime()).id(session.getId().toHexString())
                    .bookings(bookingRepository.findBySessionId(session.getId().toHexString()))
                    .build();
        } else
            throw new NoSuchElementException();

    }

    public List<Session> findByMovieId(String movieId) {
        return sessionRepository.findAllByMovieId(new ObjectId(movieId));
    }
}
