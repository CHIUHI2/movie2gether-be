package com.bootcamp.movie2gether.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String userId;
    private String userName;
    private String movieId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime LastModifiedAt;
}
