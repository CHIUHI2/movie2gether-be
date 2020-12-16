package com.bootcamp.movie2gether.review.repository;

import com.bootcamp.movie2gether.review.entity.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    Review findByMovieIdAndUserId(ObjectId movieId, ObjectId userId);
    Review findByMovieId(ObjectId movieId);
}
