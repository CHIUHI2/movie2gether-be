package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.mongodb.client.model.Field;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

@Service
public class CinemaService {
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    MongoTemplate mongoTemplate;


    public Page<Cinema> getPagedCinemas(Integer page, Integer pageSize) {
        return cinemaRepository.findAll(PageRequest.of(page, pageSize));
    }

}
