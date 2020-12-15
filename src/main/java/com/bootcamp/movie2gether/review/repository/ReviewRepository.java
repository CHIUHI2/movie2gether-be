package com.bootcamp.movie2gether.review.repository;

import com.bootcamp.movie2gether.review.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Review findAllByMovieIdAndUserId(String movieId, String userId);
    Review findAllByMovieId(String movieId);
}
