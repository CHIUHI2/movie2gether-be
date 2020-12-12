package com.bootcamp.movie2gether.payment.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class Payment {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    public Payment() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
