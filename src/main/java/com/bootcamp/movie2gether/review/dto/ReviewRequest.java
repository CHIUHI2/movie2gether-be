package com.bootcamp.movie2gether.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String userId;
    private String movieId;
    private Integer rating;
    private String comment;
}
