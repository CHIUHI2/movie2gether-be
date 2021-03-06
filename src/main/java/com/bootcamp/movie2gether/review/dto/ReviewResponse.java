package com.bootcamp.movie2gether.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String sessionId;
    private String userId;
    private String userName;
    private String movieId;
    private Integer rating;
    private String comment;
    private ZonedDateTime createdAt;
    private ZonedDateTime LastModifiedAt;
}
