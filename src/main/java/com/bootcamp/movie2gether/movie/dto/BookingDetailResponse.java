package com.bootcamp.movie2gether.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailResponse {
    private String id;
    private String userId;
    private String seatNumber;
    private SessionDetail sessionDetail;
}
