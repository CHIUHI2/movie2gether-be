package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.entity.Session;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    List<Session> findAllByMovieId(ObjectId movieId);
}
