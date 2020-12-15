package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.entity.Cinema;
import com.bootcamp.movie2gether.movie.service.CinemaService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cinemas")
@ResponseBody
@CrossOrigin
public class CinemaController {
    @Autowired
    CinemaService cinemaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<Cinema> getPagedCinemas(@RequestParam Integer page,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(required = false) String movieId) {
        if (movieId == null)
            return cinemaService.getPagedCinemas(page, pageSize);
        else
            return cinemaService.getPagedCinemasByMovieId(new ObjectId(movieId), page, pageSize);
    }
}
