package com.bootcamp.movie2gether.movie.dto;

import com.bootcamp.movie2gether.movie.entity.Booking;
import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SessionDetail {
    private String id;
    private Cinema cinema;
    private Movie movie;
    private List<Booking> bookings;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
