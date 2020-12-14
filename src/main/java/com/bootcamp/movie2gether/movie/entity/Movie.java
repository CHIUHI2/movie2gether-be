package com.bootcamp.movie2gether.movie.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.ZonedDateTime;

@Document(collection = "movies")
@Data
public class Movie {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String overview;
    private Float popularity;
    private ZonedDateTime releaseDate;
    private Integer runtime;
    private String tagline;
    private String title;
    private Float rateAverage;
    private String posterUrl;
    private Boolean onShow;
}
