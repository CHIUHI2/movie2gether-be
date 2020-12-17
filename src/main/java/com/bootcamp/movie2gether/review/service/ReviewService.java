package com.bootcamp.movie2gether.review.service;

import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieRepository movieRepository;

    public Page<Review> getByMovieIdWithPagination(String movieId, Pageable pageable) throws MovieNotFoundException {
        if(!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException();
        }

        return  reviewRepository.findByMovieId(new ObjectId(movieId), pageable);
    }

    public Review getByMovieIdAndUserId(String movieId, String userId) {
        if (!reviewRepository.existsByMovieIdAndUserId(new ObjectId(movieId), new ObjectId(userId))) {
            return null;
        }
        return reviewRepository.findByMovieIdAndUserId(new ObjectId(movieId), new ObjectId(userId));
    }

    public Review save(Review review) {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        review.setCreatedAt(currentDateTime);
        review.setLastModifiedAt(currentDateTime);

        return reviewRepository.save(review);
    }

    public Review update(String reviewId, Review review) {
        review.setId(new ObjectId(reviewId));
        review.setLastModifiedAt(ZonedDateTime.now());

        return reviewRepository.save(review);
    }
}
