package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private static final String apiBaseUrl = "/movies";

    @AfterEach
    void tearDown() {
        this.movieRepository.deleteAll();
    }
}
