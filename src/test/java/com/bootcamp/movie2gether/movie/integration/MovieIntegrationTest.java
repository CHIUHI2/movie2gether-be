package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @WithMockUser(value = "spring")
    public void should_return_the_movie_detail_when_find_by_a_valid_id_given_movies() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setTitle("testMovieTitle");
        movie = movieRepository.insert(movie);

        //when
        mockMvc.perform(get(apiBaseUrl + "/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("testMovieTitle"));
        //then
    }
}
