package com.bootcamp.movie2gether.movie.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class Movie {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    public Movie() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
