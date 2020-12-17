package com.bootcamp.movie2gether.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionBookingDetail {
    private String id;
    private List<String> seatNumbers;
    private SessionDetail sessionDetail;
}
