package com.bootcamp.movie2gether.review.mapper;

import com.bootcamp.movie2gether.review.dto.ReviewRequest;
import com.bootcamp.movie2gether.review.dto.ReviewResponse;
import com.bootcamp.movie2gether.review.entity.Review;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewRequest reviewRequest){
        Review review = new Review();
        BeanUtils.copyProperties(reviewRequest, review);
        review.setUserId(new ObjectId(reviewRequest.getUserId()));
        review.setMovieId(new ObjectId(reviewRequest.getMovieId()));
        return review;
    }

    public ReviewResponse toResponse(Review review){
        if(review == null){
            return new ReviewResponse();
        }
        ReviewResponse reviewResponse = new ReviewResponse();
        BeanUtils.copyProperties(review, reviewResponse);
        reviewResponse.setId(review.getId().toString());
        reviewResponse.setMovieId(review.getMovieId().toString());
        reviewResponse.setUserId(review.getUserId().toString());
        return reviewResponse;
    }
}
