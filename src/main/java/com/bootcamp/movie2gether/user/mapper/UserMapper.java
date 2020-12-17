package com.bootcamp.movie2gether.user.mapper;

import com.bootcamp.movie2gether.user.dto.UserProfileResponse;
import com.bootcamp.movie2gether.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileResponse toUserProfileResponse(User user) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();

        BeanUtils.copyProperties(user, userProfileResponse);
        userProfileResponse.setId(user.getId().toString());

        return userProfileResponse;
    }
}
