package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.BookingDetailResponse;
import com.bootcamp.movie2gether.movie.dto.BookingRequest;
import com.bootcamp.movie2gether.movie.dto.BookingResponse;
import com.bootcamp.movie2gether.movie.exception.AlreadyBookedException;
import com.bootcamp.movie2gether.movie.mapper.BookingMapper;
import com.bootcamp.movie2gether.movie.service.BookingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
@ResponseBody
@CrossOrigin
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BookingDetailResponse> getBookingDetails(@RequestParam Integer page,
                                                  @RequestParam Integer pageSize,
                                                  @RequestParam String userId) {
        return bookingService.getPagedBookingDetailsByUserId(new ObjectId(userId), page, pageSize);
    }
}
