package com.bootcamp.movie2gether.user.integration;

import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static final String apiBaseUrl = "/users";

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_user_when_add_friend_given_exist_id_exist_username() throws Exception {
        //given
        User user = new User();
        user = userRepository.insert(user);

        User targetUser = new User();
        targetUser.setUserName("test");
        targetUser = userRepository.insert(targetUser);

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "ADD_FRIEND");
        requestBody.put("targetUserName", targetUser.getUserName());

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + user.getId().toString() + "/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.friends", hasSize(1)))
                .andExpect(jsonPath("$.friends[0].id").value(targetUser.getId().toString()));

        User requestUser = userRepository.findById(user.getId().toString()).orElseThrow(UserNotFoundException::new);
        assertEquals(Collections.singleton(targetUser.getId()), requestUser.getFriends());

        User beRequestedUser = userRepository.findById(targetUser.getId().toString()).orElseThrow(UserNotFoundException::new);
        assertEquals(Collections.singleton(user.getId()), beRequestedUser.getFriends());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_404_when_add_friend_given_not_exist_id_exist_username() throws Exception {
        //given
        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "ADD_FRIEND");
        requestBody.put("targetUserName", "test");

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + (new ObjectId()).toString() + "/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_404_when_add_friend_given_exist_id_not_exist_username() throws Exception {
        //given
        User user = new User();
        user = userRepository.insert(user);

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "ADD_FRIEND");
        requestBody.put("targetUserName", "test");

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + user.getId().toString() + "/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_user_when_remove_friend_given_exist_id_exist_username() throws Exception {
        //given
        User targetUser = new User();
        targetUser.setUserName("test");
        targetUser = userRepository.insert(targetUser);

        User user = new User();
        user.setFriends(Collections.singleton(targetUser.getId()));
        user = userRepository.insert(user);

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "UNFRIEND");
        requestBody.put("targetUserName", targetUser.getUserName());

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + user.getId().toString() + "/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.friends", hasSize(0)));

        User requestUser = userRepository.findById(user.getId().toString()).orElseThrow(UserNotFoundException::new);
        assertEquals(Collections.emptySet(), requestUser.getFriends());

        User beRequestedUser = userRepository.findById(targetUser.getId().toString()).orElseThrow(UserNotFoundException::new);
        assertEquals(Collections.emptySet(), beRequestedUser.getFriends());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_404_when_remove_friend_given_not_exist_id_exist_username() throws Exception {
        //given
        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "UNFRIEND");
        requestBody.put("targetUserName", "test");

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + (new ObjectId()).toString() + "/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_404_when_remove_friend_given_exist_id_not_exist_username() throws Exception {
        //given
        User user = new User();
        user = userRepository.insert(user);

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", "UNFRIEND");
        requestBody.put("targetUserName", "test");

        //when
        //then
        mockMvc.perform(patch(apiBaseUrl + "/" + user.getId().toString() + "/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isNotFound());
    }
}
