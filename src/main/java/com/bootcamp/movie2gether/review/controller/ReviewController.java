package com.bootcamp.movie2gether.review.controller;

import com.bootcamp.movie2gether.review.dto.ReviewRequest;
import com.bootcamp.movie2gether.review.dto.ReviewResponse;
import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.mapper.ReviewMapper;
import com.bootcamp.movie2gether.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewService reviewService;

    @GetMapping(params={"movieId","userId"})
    public ReviewResponse getByMovieIdAndUserId(@RequestParam String movieId, @RequestParam String userId){
        return reviewMapper.toResponse(reviewService.getByMovieIdAndUserId(movieId,userId));
    }

}
