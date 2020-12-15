package com.bootcamp.movie2gether.movie.dto;

import lombok.Data;

@Data
public class MovieListingResponse {
    private String id;
    private String title;
    private String posterUrl;
}
