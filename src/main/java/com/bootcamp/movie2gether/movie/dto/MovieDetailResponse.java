package com.bootcamp.movie2gether.movie.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MovieDetailResponse {
    private String id;
    private String overview;
    private Float popularity;
    private ZonedDateTime releaseDate;
    private Integer runtime;
    private String tagline;
    private String title;
    private Float voteAverage;
    private String posterUrl;
    private Boolean onShow;
    private List<String> genres;
}
