package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends MongoRepository<Cinema, String> {

}
