package com.bootcamp.movie2gether.friend.controller;

import com.bootcamp.movie2gether.friend.dto.AddFriendRequest;
import com.bootcamp.movie2gether.friend.dto.AddFriendResponse;
import com.bootcamp.movie2gether.friend.dto.FriendResponse;
import com.bootcamp.movie2gether.friend.entity.Friend;
import com.bootcamp.movie2gether.friend.mapper.FriendMapper;

import com.bootcamp.movie2gether.friend.service.FriendService;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
@CrossOrigin
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendMapper friendMapper;

    @GetMapping("/{userId}")
    public FriendResponse getFriend(@PathVariable String userId) {
        return friendMapper.toResponse(friendService.findByUserId(userId));
    }

    @PostMapping
    public Friend add(@RequestBody AddFriendRequest addFriendRequest) throws UserNotFoundException {
        return friendService.add(addFriendRequest);
    }

    @DeleteMapping("/{friendId}")
    public FriendResponse delete(@PathVariable String friendId) throws UserNotFoundException {
        return friendMapper.toResponse(friendService.delete(friendId));
    }
}
