package com.bootcamp.movie2gether.user.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
public class User {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String userName;
    private String email;
    private String password;

    public User(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

}
