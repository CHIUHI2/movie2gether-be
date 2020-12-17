package com.bootcamp.movie2gether.user.service;

import com.bootcamp.movie2gether.user.dto.FriendRequest;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void should_return_user_when_find_by_id_given_exist_id() throws UserNotFoundException {
        //give
        User user = new User();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        //when
        User returnedUser = userService.findById("1");

        //then
        assertEquals(user, returnedUser);
    }

    @Test
    void should_throw_user_not_found_exception_when_find_by_id_given_not_exist_id() {
        //give
        User user = new User();

        when(userRepository.findById("1")).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            //when
            userService.findById("1");
        });
    }

    @Test
    void should_return_updated_user_when_add_friend_given_exist_id_exist_friend_username() throws UserNotFoundException {
        //given
        User user = new User();
        user.setId(new ObjectId());
        User targetUser = new User();
        targetUser.setId(new ObjectId());

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findByUserName("test")).thenReturn(Optional.of(targetUser));

        user.setFriends(Collections.singleton(targetUser.getId()));

        when(userRepository.save(targetUser)).thenReturn(targetUser);
        when(userRepository.save(user)).thenReturn(user);

        FriendRequest friendRequest = new FriendRequest("test", "ADD_FRIEND");

        //when
        User returnedUser = userService.addFriend(friendRequest, "1");

        //then
        assertEquals(user, returnedUser);
    }

    @Test
    void should_throw_user_not_found_exception_when_add_friend_given_exist_id_not_exist_friend_username() {
        //given
        User user = new User();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findByUserName("test")).thenReturn(Optional.empty());

        FriendRequest friendRequest = new FriendRequest("test", "ADD_FRIEND");

        //then
        assertThrows(UserNotFoundException.class, () -> {
            //when
            userService.addFriend(friendRequest, "1");
        });
    }

    @Test
    void should_throw_user_not_found_exception_when_add_friend_given_not_exist_id_exist_friend_username() {
        //given
        User targetUser = new User();

        when(userRepository.findById("1")).thenReturn(Optional.empty());
        when(userRepository.findByUserName("test")).thenReturn(Optional.of(targetUser));

        FriendRequest friendRequest = new FriendRequest("test", "ADD_FRIEND");

        //then
        assertThrows(UserNotFoundException.class, () -> {
            //when
            userService.addFriend(friendRequest, "1");
        });
    }

    @Test
    void should_return_updated_user_when_remove_friend_given_exist_id_exist_friend_username() throws UserNotFoundException {
        //given
        User user = new User();
        user.setId(new ObjectId());
        User targetUser = new User();
        targetUser.setId(new ObjectId());

        user.setFriends(Collections.singleton(targetUser.getId()));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findByUserName("test")).thenReturn(Optional.of(targetUser));

        user.setFriends(Collections.emptySet());

        when(userRepository.save(targetUser)).thenReturn(targetUser);
        when(userRepository.save(user)).thenReturn(user);

        FriendRequest friendRequest = new FriendRequest("test", "UNFRIEND");

        //when
        User returnedUser = userService.removeFriend(friendRequest, "1");

        //then
        assertEquals(user, returnedUser);
    }

    @Test
    void should_throw_user_not_found_exception_when_remove_friend_given_exist_id_not_exist_friend_username() {
        //given
        User user = new User();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findByUserName("test")).thenReturn(Optional.empty());

        FriendRequest friendRequest = new FriendRequest("test", "ADD_FRIEND");

        //then
        assertThrows(UserNotFoundException.class, () -> {
            //when
            userService.removeFriend(friendRequest, "1");
        });
    }

    @Test
    void should_throw_user_not_found_exception_when_remove_friend_given_not_exist_id_exist_friend_username() {
        //given
        User targetUser = new User();

        when(userRepository.findById("1")).thenReturn(Optional.empty());
        when(userRepository.findByUserName("test")).thenReturn(Optional.of(targetUser));

        FriendRequest friendRequest = new FriendRequest("test", "ADD_FRIEND");

        //then
        assertThrows(UserNotFoundException.class, () -> {
            //when
            userService.removeFriend(friendRequest, "1");
        });
    }
}
