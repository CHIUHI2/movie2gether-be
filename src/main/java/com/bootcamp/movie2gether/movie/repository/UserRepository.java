package com.bootcamp.movie2gether.movie.repository;

import com.bootcamp.movie2gether.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
