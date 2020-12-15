package com.bootcamp.movie2gether.movie.integration;

import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

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
        //then
        mockMvc.perform(get(apiBaseUrl + "/" + movie.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movie.getId().toString()))
                .andExpect(jsonPath("$.title").value("testMovieTitle"));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_not_found_when_find_by_an_invalid_id_given_movies() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get(apiBaseUrl + "/" + new ObjectId().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_on_show_movie_when_find_on_show_movie_given_movies() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieRepository.insert(movieOnShow1);

        Movie movieOnShow2 = new Movie();
        movieOnShow2.setTitle("movieOnShow2");
        movieOnShow2.setReleaseDate(ZonedDateTime.now());
        movieRepository.insert(movieOnShow2);

        Movie movieComingSoon = new Movie();
        movieComingSoon.setTitle("movieComingSoon");
        movieComingSoon.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieRepository.insert(movieComingSoon);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                        .param("mode", "onShow")
                        .param("isRecommend", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieOnShow1.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(movieOnShow2.getId().toString()));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_on_show_movie_with_sorted_popularity_when_find_on_show_movie_given_movies() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieOnShow1.setPopularity(Float.valueOf("10"));
        movieRepository.insert(movieOnShow1);

        Movie movieOnShow2 = new Movie();
        movieOnShow2.setTitle("movieOnShow2");
        movieOnShow2.setReleaseDate(ZonedDateTime.now());
        movieOnShow2.setPopularity(Float.valueOf("20"));
        movieRepository.insert(movieOnShow2);

        Movie movieComingSoon = new Movie();
        movieComingSoon.setTitle("movieComingSoon");
        movieComingSoon.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieRepository.insert(movieComingSoon);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                .param("mode", "onShow")
                .param("isRecommend", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieOnShow2.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(movieOnShow1.getId().toString()));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_coming_soon_movie_when_find_coming_soon_movie_given_movies() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieRepository.insert(movieOnShow1);

        Movie movieOnShow2 = new Movie();
        movieOnShow2.setTitle("movieOnShow2");
        movieOnShow2.setReleaseDate(ZonedDateTime.now());
        movieRepository.insert(movieOnShow2);

        Movie movieComingSoon = new Movie();
        movieComingSoon.setTitle("movieComingSoon");
        movieComingSoon.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieRepository.insert(movieComingSoon);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                .param("mode", "coming")
                .param("isRecommend", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieComingSoon.getId().toString()));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_coming_soon_movie_with_sorted_popularity_when_find_coming_soon_movie_given_movies() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieRepository.insert(movieOnShow1);

        Movie movieComingSoon1 = new Movie();
        movieComingSoon1.setTitle("movieComingSoon1");
        movieComingSoon1.setPopularity(Float.valueOf("10"));
        movieComingSoon1.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieRepository.insert(movieComingSoon1);

        Movie movieComingSoon2 = new Movie();
        movieComingSoon2.setTitle("movieComingSoon2");
        movieComingSoon2.setPopularity(Float.valueOf("20"));
        movieComingSoon2.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieRepository.insert(movieComingSoon2);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                .param("mode", "coming")
                .param("isRecommend", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieComingSoon2.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(movieComingSoon1.getId().toString()));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_on_show_movie_with_genre_action_when_find_on_show_movie_by_genre_given_movies_genre_action() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieOnShow1.setGenres(Arrays.asList("action", "comedy"));
        movieRepository.insert(movieOnShow1);

        Movie movieOnShow2 = new Movie();
        movieOnShow2.setTitle("movieOnShow2");
        movieOnShow2.setReleaseDate(ZonedDateTime.now());
        movieOnShow2.setGenres(Collections.singletonList("comedy"));
        movieRepository.insert(movieOnShow2);

        Movie movieComingSoon = new Movie();
        movieComingSoon.setTitle("movieComingSoon");
        movieComingSoon.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieComingSoon.setGenres(Collections.singletonList("action"));
        movieRepository.insert(movieComingSoon);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                .param("mode", "onShow")
                .param("genre", "action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieOnShow1.getId().toString()));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_coming_soon_movie_with_genre_action_when_find_coming_soon_movie_by_genre_given_movies_genre_action() throws Exception {
        //given
        Movie movieOnShow1 = new Movie();
        movieOnShow1.setTitle("movieOnShow1");
        movieOnShow1.setReleaseDate(ZonedDateTime.now().minusDays(1));
        movieOnShow1.setGenres(Arrays.asList("action", "comedy"));
        movieRepository.insert(movieOnShow1);

        Movie movieOnShow2 = new Movie();
        movieOnShow2.setTitle("movieOnShow2");
        movieOnShow2.setReleaseDate(ZonedDateTime.now());
        movieOnShow2.setGenres(Collections.singletonList("comedy"));
        movieRepository.insert(movieOnShow2);

        Movie movieComingSoon = new Movie();
        movieComingSoon.setTitle("movieComingSoon");
        movieComingSoon.setReleaseDate(ZonedDateTime.now().plusDays(1));
        movieComingSoon.setGenres(Collections.singletonList("action"));
        movieRepository.insert(movieComingSoon);

        //when
        //then
        mockMvc.perform(get(apiBaseUrl)
                .param("mode", "coming")
                .param("genre", "action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(movieComingSoon.getId().toString()));
    }
}
