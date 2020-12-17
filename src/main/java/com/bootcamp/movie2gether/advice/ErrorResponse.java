package com.bootcamp.movie2gether.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final String status;
    private final String message;

}
