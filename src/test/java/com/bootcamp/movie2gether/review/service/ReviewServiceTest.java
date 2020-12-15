package com.bootcamp.movie2gether.review.service;

import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
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
        when(reviewRepository.findByMovieIdAndUserId(any(),any())).thenReturn(expected);

        //when
        final Review review = reviewService.getByMovieIdAndUserId("5fd77c99e5f7d6417d7abac4","5fd81ac741ea7016828cfd40");

        //then
        assertEquals(expected,review);
    }
}
