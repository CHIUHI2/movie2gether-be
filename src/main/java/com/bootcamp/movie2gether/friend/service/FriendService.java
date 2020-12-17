package com.bootcamp.movie2gether.friend.service;

import com.bootcamp.movie2gether.friend.dto.AddFriendRequest;
import com.bootcamp.movie2gether.friend.entity.Friend;
import com.bootcamp.movie2gether.friend.repository.FriendRepository;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    public Friend findByUserId(String userId) {
        if(!friendRepository.findByUserId(userId).isPresent()){
            Friend friend = new Friend();
            friend.setUserId(new ObjectId(userId));
            friend.setFriendIdList(new ArrayList<>());
            return friendRepository.insert(friend);
        }
        return friendRepository.findByUserId(userId).get();
    }

    public Friend add(AddFriendRequest addFriendRequest) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUserName(addFriendRequest.getFriendName());
        if (user.isPresent())
            throw new UserNotFoundException();
        Friend friend = friendRepository.findByUserId(addFriendRequest.getUserId()).get();
        List<ObjectId> friendIdList = friend.getFriendIdList();
        friendIdList.add(user.get().getId());
        friend.setFriendIdList(friendIdList);
        return friendRepository.save(friend);
    }

    public Friend delete(String id) throws UserNotFoundException {
        if (friendRepository.existsById(id)) {
            Friend friend = friendRepository.findById(id).get();
            friendRepository.deleteById(id);
            return friend;
        }
        throw new UserNotFoundException();
    }

}
