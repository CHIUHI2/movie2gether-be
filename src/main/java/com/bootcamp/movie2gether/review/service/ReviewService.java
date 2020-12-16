package com.bootcamp.movie2gether.review.service;

import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public Review getByMovieIdAndUserId(String movieId, String userId) {
        if (!reviewRepository.existsByMovieIdAndUserId(new ObjectId(movieId), new ObjectId(userId))) {
            return null;
        }
        return reviewRepository.findByMovieIdAndUserId(new ObjectId(movieId), new ObjectId(userId));
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review update(String reviewId, Review review) {
        review.setId(new ObjectId(reviewId));
        return reviewRepository.save(review);
    }
}
