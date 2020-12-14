package com.bootcamp.movie2gether.movie.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collation = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
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
