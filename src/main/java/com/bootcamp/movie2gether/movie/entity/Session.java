package com.bootcamp.movie2gether.movie.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.ZonedDateTime;

@Document
@Data
public class Session {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String cinemaId;
    private String movieId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
