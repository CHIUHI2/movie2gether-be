package com.bootcamp.movie2gether.review.service;

import com.bootcamp.movie2gether.movie.exception.MovieNotFoundException;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.review.dto.ReviewResponse;
import com.bootcamp.movie2gether.review.entity.Review;
import com.bootcamp.movie2gether.review.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    MovieRepository movieRepository;

    @Test
    public void should_return_review_from_a_specific_user_of_a_specific_movie_when_get_by_movieId_and_userId_given_movieId_and_userId() {
        //given
        final Review expected = new Review();
        when(reviewRepository.existsBySessionIdAndMovieIdAndUserId(any(),any(),any())).thenReturn(true);
        when(reviewRepository.findBySessionIdAndMovieIdAndUserId(any(), any(),any())).thenReturn(expected);

        //when
        final Review review = reviewService.getBySessionIdAndMovieIdAndUserId("5fd9d86f03c5cf30ed507c59", "5fd77c99e5f7d6417d7abac4","5fd81ac741ea7016828cfd40");

        //then
        assertEquals(expected,review);
    }

    @Test
    public void should_return_created_review_when_save_given_no_review_in_database() {
        //given
        final Review expected = new Review();
        when(reviewRepository.save(expected)).thenReturn(expected);

        //when
        reviewService.save(expected);
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewArgumentCaptor.capture());

        //then
        final Review review = reviewArgumentCaptor.getValue();
        assertEquals(expected,review);
    }

    @Test
    public void should_return_updated_review_when_update_given_valid_review_id() {
        //given
        final Review updatedReview = new Review();
        when(reviewRepository.save(updatedReview)).thenReturn(updatedReview);

        //when
        final Review review = reviewService.update("5fd8d11c30fcb742001d2376",updatedReview);

        //then
        assertEquals(updatedReview, review);
    }

    @Test
    public void should_return_review_page_when_get_by_movie_id_with_pagination_given_existed_movie_id_page_size_5_page_1() throws MovieNotFoundException {
        //given
        ObjectId movieId = new ObjectId();
        Review review = new Review();
        Pageable pageable = PageRequest.of(1, 5);
        Page<Review> expectedReviewPage = new PageImpl<>(Collections.singletonList(review), pageable, 1);

        when(movieRepository.existsById(movieId.toString())).thenReturn(true);
        when(reviewRepository.findByMovieIdAndSessionIdNotNull(movieId, pageable)).thenReturn(expectedReviewPage);

        //when
        Page<Review> reviewPage = reviewService.getByMovieIdWithPagination(movieId.toString(), pageable);

        //then
        assertEquals(expectedReviewPage, reviewPage);
    }

    @Test
    public void should_throw_movie_not_found_exception_when_get_by_movie_id_give_pagination_given_not_existed_movie_id_page_size_5_page_1() {
        //given
        ObjectId movieId = new ObjectId();
        Pageable pageable = PageRequest.of(1, 5);

        when(movieRepository.existsById(movieId.toString())).thenReturn(false);

        //when
        assertThrows(MovieNotFoundException.class, () -> {
            //then
            reviewService.getByMovieIdWithPagination(movieId.toString(), pageable);
        });
    }
}
