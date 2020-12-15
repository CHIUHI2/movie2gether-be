package com.bootcamp.movie2gether.review.service;

import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;

    @Test
    public void should_return_review_from_a_specific_user_of_a_specific_movie_when_get_by_movieId_and_userId_given_ids_are_valid() {
        //given
        final Review expected = new Review();
        when(reviewRepository.findAllByMovieIdAndUserId(anyString(),anyString())).thenReturn(expected);

        //when
        final Review review = reviewService.getByMovieIdAndUserId("1","1");

        //then
        assertEquals(expected,review);
    }
}
