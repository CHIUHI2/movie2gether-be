package com.bootcamp.movie2gether.movie.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Seat {
    private String id;
    private String number;
    private Float x;
    private Float y;
}
