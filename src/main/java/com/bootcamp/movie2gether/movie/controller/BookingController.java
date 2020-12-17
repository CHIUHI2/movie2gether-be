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

import java.util.List;
import java.util.stream.Collectors;

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
    List<BookingResponse> book(@RequestBody BookingRequest bookingRequest) throws AlreadyBookedException {
        if (bookingRequest.getUserId() == null || bookingRequest.getSessionId() == null || bookingRequest.getSeatNumbers() == null)
            throw new IllegalArgumentException("Invalid BookingRequest");
        return bookingService.book(
                new ObjectId(bookingRequest.getUserId()),
                new ObjectId(bookingRequest.getSessionId()),
                bookingRequest.getSeatNumbers()
        )
                .stream()
                .map(booking -> bookingMapper.toResponse(booking))
                .collect(Collectors.toList());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BookingDetailResponse> getBookingDetails(@RequestParam Integer page,
                                                  @RequestParam Integer pageSize,
                                                  @RequestParam String userId) {
        return bookingService.getPagedBookingDetailsByUserId(new ObjectId(userId), page, pageSize);
    }
}
