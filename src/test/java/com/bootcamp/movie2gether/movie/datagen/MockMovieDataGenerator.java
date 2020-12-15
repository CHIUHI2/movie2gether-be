package com.bootcamp.movie2gether.movie.datagen;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.entity.Movie;
import com.bootcamp.movie2gether.movie.entity.Seat;
import com.bootcamp.movie2gether.movie.entity.Session;
import com.bootcamp.movie2gether.movie.repository.BookingRepository;
import com.bootcamp.movie2gether.movie.repository.CinemaRepository;
import com.bootcamp.movie2gether.movie.repository.MovieRepository;
import com.bootcamp.movie2gether.movie.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
public class MockMovieDataGenerator {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void deleteAllDocuments() {
        movieRepository.deleteAll();
        cinemaRepository.deleteAll();
        sessionRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void addMovieCinemaSession() {
        List<Movie> movies = movieRepository.saveAll(
                IntStream.range(0, 10)
                        .mapToObj(i -> Movie.builder()
                                .title(String.format("Movie %d", i))
                                .onShow(true)
                                .build())
                        .collect(Collectors.toList())
        );
        List<Cinema> cinemas = cinemaRepository.saveAll(
                IntStream.range(0, 10)
                        .mapToObj(i -> Cinema.builder()
                                .name(String.format("Cinema %d", i))
                                .seats(
                                        IntStream.range(0, 10)
                                                .mapToObj(seatNum -> new Seat(String.format("A%d", seatNum)))
                                                .collect(Collectors.toList())
                                )
                                .build())
                        .collect(Collectors.toList())
        );
        sessionRepository.saveAll(
                IntStream.range(0, 100)
                        .mapToObj(i -> Session.builder()
                                .movieId(movies.get(ThreadLocalRandom.current().nextInt(movies.size())).getId())
                                .cinemaId(cinemas.get(ThreadLocalRandom.current().nextInt(cinemas.size())).getId())
                                .startTime(LocalDateTime.now().plusHours(i))
                                .endTime(LocalDateTime.now().plusHours(i + 2))
                                .build()
                        )
                        .collect(Collectors.toList()));
    }
}
