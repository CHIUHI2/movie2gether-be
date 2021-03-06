package com.bootcamp.movie2gether.user.controller;

import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.user.dto.FriendRequest;
import com.bootcamp.movie2gether.user.dto.UserProfileResponse;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.FriendRequestActionInvalidException;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.mapper.UserMapper;
import com.bootcamp.movie2gether.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    private static final String FRIEND_REQUEST_ACTION_ADD_FRIEND = "ADD_FRIEND";
    private static final String FRIEND_REQUEST_ACTION_UNFRIEND = "UNFRIEND";

    @GetMapping("/{id}")
    public UserProfileResponse getById(@PathVariable String id) throws UserNotFoundException {
        User user = userService.findById(id);

        List<UserProfileResponse> friendList = user.getFriends().stream()
                .map(userId -> {
                    try {
                        return userMapper.toUserProfileResponse(userService.findById(userId.toString()));
                    } catch (UserNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        UserProfileResponse userProfileResponse = userMapper.toUserProfileResponse(user);
        userProfileResponse.setFriends(friendList);

        return userProfileResponse;
    }

    @PatchMapping("/{id}/friends")
    public UserProfileResponse friendsAction(@RequestBody FriendRequest friendRequest, @PathVariable String id) throws UserNotFoundException, FriendRequestActionInvalidException {
        String action = friendRequest.getAction();

        if(FRIEND_REQUEST_ACTION_ADD_FRIEND.equalsIgnoreCase(action)) {
            User user = userService.addFriend(friendRequest, id);
            List<UserProfileResponse> friends = user.getFriends().stream()
                    .map(userId -> {
                        try {
                            return userMapper.toUserProfileResponse(userService.findById(userId.toString()));
                        } catch (UserNotFoundException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            UserProfileResponse userProfileResponse = userMapper.toUserProfileResponse(user);
            userProfileResponse.setFriends(friends);

            return userProfileResponse;
        }
        else if(FRIEND_REQUEST_ACTION_UNFRIEND.equalsIgnoreCase(action)) {
            User user = userService.removeFriend(friendRequest, id);
            List<UserProfileResponse> friends = user.getFriends().stream()
                    .map(userId -> {
                        try {
                            return userMapper.toUserProfileResponse(userService.findById(userId.toString()));
                        } catch (UserNotFoundException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            UserProfileResponse userProfileResponse = userMapper.toUserProfileResponse(user);
            userProfileResponse.setFriends(friends);

            return userProfileResponse;
        }
        else {
            throw new FriendRequestActionInvalidException();
        }
    }
}
