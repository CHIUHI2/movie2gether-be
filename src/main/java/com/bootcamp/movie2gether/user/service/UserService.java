package com.bootcamp.movie2gether.user.service;

import com.bootcamp.movie2gether.user.dto.FriendRequest;
import com.bootcamp.movie2gether.user.dto.RegisterRequest;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.EmptyInputException;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.exceptions.WeakPasswordException;
import com.bootcamp.movie2gether.user.exceptions.WrongEmailFormatException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void validateRegistrationRequest(RegisterRequest registerRequest) {
        if (registerRequest.getUserName().isEmpty()) {
            throw new EmptyInputException();
        }

        if (registerRequest.getEmail().isEmpty()) {
            throw new EmptyInputException();
        }

        if (registerRequest.getPassword().isEmpty()) {
            throw new EmptyInputException();
        }

        if (!registerRequest.getEmail().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new WrongEmailFormatException();
        }

        if (!registerRequest.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z.!#$%&'*+/=?^_`{|}~-]{8,}$")) {
            throw new WeakPasswordException();
        }

    }

    public User findById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User addFriend(FriendRequest friendRequest, String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        Optional<User> targetUserOptional = userRepository.findByUserName(friendRequest.getTargetUserName());
        if(!userOptional.isPresent() || !targetUserOptional.isPresent()) {
            throw new UserNotFoundException();
        }

        User user = userOptional.get();
        User targetUser = targetUserOptional.get();

        Set<ObjectId> userFriendIdSet = new HashSet<>(user.getFriends());
        userFriendIdSet.add(targetUser.getId());
        user.setFriends(userFriendIdSet);

        Set<ObjectId> targetUserFriendIdSet = new HashSet<>(targetUser.getFriends());
        targetUserFriendIdSet.add(user.getId());
        targetUser.setFriends(targetUserFriendIdSet);

        userRepository.save(targetUser);

        return userRepository.save(user);
    }

    public User removeFriend(FriendRequest friendRequest, String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        Optional<User> targetUserOptional = userRepository.findByUserName(friendRequest.getTargetUserName());
        if(!userOptional.isPresent() || !targetUserOptional.isPresent()) {
            throw new UserNotFoundException();
        }

        User user = userOptional.get();
        User targetUser = targetUserOptional.get();

        Set<ObjectId> userFriendIdSet = new HashSet<>(user.getFriends());
        userFriendIdSet.remove(targetUser.getId());
        user.setFriends(userFriendIdSet);

        Set<ObjectId> targetUserFriendIdSet = new HashSet<>(targetUser.getFriends());
        targetUserFriendIdSet.remove(user.getId());
        targetUser.setFriends(targetUserFriendIdSet);

        userRepository.save(targetUser);

        return userRepository.save(user);
    }
}
