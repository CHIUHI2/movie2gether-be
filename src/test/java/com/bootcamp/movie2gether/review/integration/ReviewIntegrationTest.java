package com.bootcamp.movie2gether.review.integration;

import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown(){
        reviewRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_review_from_specific_user_of_specific_movie_when_get_review_given_userId_and_movieId() throws Exception {
        //given
        Review review = new Review();
        review.setMovieId(new ObjectId("5fd77c99e5f7d6417d7abac9"));
        review.setUserId(new ObjectId("5fd81ac741ea7016828cfd39"));
        review.setRating(4);
        review.setComment("funny movie");
        reviewRepository.save(review);

        //when
        //then
        mockMvc.perform(get("/reviews").param("movieId", "5fd77c99e5f7d6417d7abac9").param("userId","5fd81ac741ea7016828cfd39"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("funny movie"));
    }

    @Test
    @WithMockUser(value = "spring")
    public void should_return_bad_request_when_get_review_given_invalid_userId_and_invalid_movieId() throws Exception {
        //given
        Review review = new Review();
        review.setMovieId(new ObjectId("5fd77c99e5f7d6417d7abac9"));
        review.setUserId(new ObjectId("5fd81ac741ea7016828cfd39"));
        review.setRating(4);
        review.setComment("funny movie");
        reviewRepository.save(review);

        //when
        //then
        mockMvc.perform(get("/reviews").param("movieId", "5fd77c99e5f7d6417d7abac1").param("userId","5fd81ac741ea7016828cfd31"))
                .andExpect(status().isBadRequest());
    }
}
