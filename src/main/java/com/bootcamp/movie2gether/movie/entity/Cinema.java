package com.bootcamp.movie2gether.movie.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Data
public class Cinema {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String address;
    private String name;
    private List<Seat> seats;
}
