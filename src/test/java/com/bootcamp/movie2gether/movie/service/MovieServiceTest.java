package com.bootcamp.movie2gether.movie.service;

import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;
}
