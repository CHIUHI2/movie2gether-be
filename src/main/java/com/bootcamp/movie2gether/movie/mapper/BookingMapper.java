package com.bootcamp.movie2gether.movie.mapper;

import com.bootcamp.movie2gether.movie.dto.BookingResponse;
import com.bootcamp.movie2gether.movie.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId().toHexString())
                .userId(booking.getUserId().toHexString())
                .seatNumber(booking.getSeatNumber())
                .sessionId(booking.getSessionId().toHexString())
                .build();
    }
}
