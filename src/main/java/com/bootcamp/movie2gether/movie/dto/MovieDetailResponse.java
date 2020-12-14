package com.bootcamp.movie2gether.movie.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MovieDetailResponse {
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
