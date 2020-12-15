package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.BookingRequest;
import com.bootcamp.movie2gether.movie.dto.BookingResponse;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.mapper.BookingMapper;
import com.bootcamp.movie2gether.movie.service.BookingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
@ResponseBody
public class BookingController {
    @Autowired
    BookingService bookingService;
    @Autowired
    BookingMapper bookingMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookingResponse book(@RequestBody BookingRequest bookingRequest) throws AlreadyBookedException {
        return bookingMapper.toResponse(
                bookingService.book(new ObjectId(),
                        new ObjectId(bookingRequest.getSessionId()),
                        bookingRequest.getSeatNumber())
        );
    }
}
