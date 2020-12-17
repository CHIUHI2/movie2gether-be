package com.bootcamp.movie2gether.review.controller;

import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.review.dto.ReviewRequest;
import com.bootcamp.movie2gether.review.dto.ReviewResponse;
import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.mapper.ReviewMapper;
import com.bootcamp.movie2gether.review.service.ReviewService;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping(params={"movieId","page"})
    public Page<ReviewResponse> getByMovieIdWithPagination(@RequestParam String movieId, @RequestParam Integer page) throws MovieNotFoundException {
        Pageable pageable = PageRequest.of((page > 0 ? page - 1 : 0), 5);
        Page<Review> reviewPage = reviewService.getByMovieIdWithPagination(movieId, pageable);

        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream()
                .map((review) -> {
                        ReviewResponse reviewResponse = reviewMapper.toResponse(review);
                        try {
                            reviewResponse.setUserName(userService.findUserById(review.getUserId().toString()).getUsername());
                        } catch (UserNotFoundException e) {
                            reviewResponse.setUserName("Anonymous");
                        }

                        return reviewResponse;
                    })
                .collect(Collectors.toList());

        return new PageImpl<>(
                reviewResponses,
                pageable,
               reviewPage.getTotalElements()
        );
    }

    @GetMapping(params={"movieId","userId"})
    public ReviewResponse getByMovieIdAndUserId(@RequestParam String movieId, @RequestParam String userId){
        return reviewMapper.toResponse(reviewService.getByMovieIdAndUserId(movieId,userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse save(@RequestBody ReviewRequest reviewRequest){
        return reviewMapper.toResponse(reviewService.save(reviewMapper.toEntity(reviewRequest)));
    }

    @PutMapping("/{reviewId}")
    public ReviewResponse update(@PathVariable String reviewId, @RequestBody ReviewRequest reviewRequest){
        return reviewMapper.toResponse(reviewService.update(reviewId, reviewMapper.toEntity(reviewRequest)));
    }

}
