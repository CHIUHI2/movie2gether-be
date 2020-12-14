package com.bootcamp.movie2gether.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collation = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String overview;
    private Float popularity;
    private LocalDateTime releaseDate;
    private Integer runtime;
    private String tagline;
    private String title;
    private Float rateAverage;
    private String posterUrl;
    private Boolean onShow;
}
