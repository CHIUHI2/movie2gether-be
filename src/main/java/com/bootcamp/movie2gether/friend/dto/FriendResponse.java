package com.bootcamp.movie2gether.friend.dto;

import com.bootcamp.movie2gether.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {
    private String id;
    private String userId;
    private List<User> friends;
}
