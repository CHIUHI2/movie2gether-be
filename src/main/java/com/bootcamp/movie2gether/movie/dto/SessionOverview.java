package com.bootcamp.movie2gether.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SessionOverview {
    private String id;
    private String cinemaId;
    private String cinemaName;
    private String movieId;
    private String movieName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    private Integer availableSeats;
}
