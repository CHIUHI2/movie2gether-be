package com.bootcamp.movie2gether.review.mapper;

import com.bootcamp.movie2gether.review.dto.ReviewRequest;
import com.bootcamp.movie2gether.review.dto.ReviewResponse;
import com.bootcamp.movie2gether.review.entity.Review;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewRequest reviewRequest){
        Review review = new Review();
        BeanUtils.copyProperties(reviewRequest, review);
        return review;
    }

    public ReviewResponse toResponse(Review review){
        ReviewResponse reviewResponse = new ReviewResponse();
        BeanUtils.copyProperties(review, reviewResponse);
        return reviewResponse;
    }
}
