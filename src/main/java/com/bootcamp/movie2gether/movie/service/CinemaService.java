package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    MongoTemplate mongoTemplate;


    public Page<Cinema> getPagedCinemas(Integer page, Integer pageSize) {
        return cinemaRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Page<Cinema> getPagedCinemasByMovieId(ObjectId movieId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AggregationOperation> basePipeline = Arrays.asList(
                Aggregation.lookup("session", "_id", "cinemaId", "sessions"),
                Aggregation.addFields().addField("sessionIds").withValueOfExpression("{\n" +
                        "    \"$map\": {\n" +
                        "        \"input\": \"$sessions\",\n" +
                        "        \"as\": \"el\",\n" +
                        "        \"in\": \"$$el.movieId\"\n" +
                        "    }\n" +
                        "  }").build(),
                Aggregation.lookup("movies", "sessionIds", "_id", "movie"),
                Aggregation.unwind("movie"),
                Aggregation.match(Criteria.where("movie._id").is(movieId)),
                Aggregation.group("_id").first("name").as("name")
                        .first("address").as("address")
                        .first("seats").as("seats")
        );
        List<AggregationOperation> dataPipeline = Stream.concat(
                basePipeline.stream(),
                Stream.of(
                        Aggregation.skip((long) page * pageSize),
                        Aggregation.limit(pageSize)
                )
        ).collect(Collectors.toList());
        Aggregation dataAggregation = Aggregation.newAggregation(dataPipeline);

        List<AggregationOperation> countPipeline = Stream.concat(
                basePipeline.stream(),
                Stream.of(
                        Aggregation.count().as("count")
                )
        ).collect(Collectors.toList());
        Aggregation countAggregation = Aggregation.newAggregation(countPipeline);

        List<Cinema> cinemas = mongoTemplate.aggregate(dataAggregation, "cinema", Cinema.class).getMappedResults();
        long count = Long.parseLong(mongoTemplate.aggregate(countAggregation, "cinema", Document.class).getMappedResults().get(0).get("count").toString());

        return new PageImpl<>(cinemas, pageable, count);
    }
}
