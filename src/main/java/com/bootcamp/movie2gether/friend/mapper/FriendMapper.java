package com.bootcamp.movie2gether.friend.mapper;

import com.bootcamp.movie2gether.friend.dto.FriendResponse;
import com.bootcamp.movie2gether.friend.entity.Friend;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FriendMapper {

    public FriendResponse toResponse(Friend friend){
        FriendResponse friendResponse = new FriendResponse();
        BeanUtils.copyProperties(friend, friendResponse);
        return friendResponse;
    }
}
