package com.bootcamp.movie2gether.friend.dto;

import com.bootcamp.movie2gether.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendResponse {
    private String id;
    private String userId;
    private User friend;
}
