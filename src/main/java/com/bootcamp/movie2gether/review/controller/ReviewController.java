package com.bootcamp.movie2gether.review.controller;

import com.bootcamp.movie2gether.review.mapper.ReviewMapper;
import com.bootcamp.movie2gether.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewService reviewService;

}
