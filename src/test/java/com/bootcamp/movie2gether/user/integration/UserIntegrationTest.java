package com.bootcamp.movie2gether.user.integration;

import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
}
