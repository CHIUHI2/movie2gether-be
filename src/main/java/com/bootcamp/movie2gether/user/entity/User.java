package com.bootcamp.movie2gether.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private String userName;
    private String email;
    private String password;
    private Set<ObjectId> friends = new HashSet<>();

    public User(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.friends = new HashSet<>();
    }
}
