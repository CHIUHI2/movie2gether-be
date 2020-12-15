package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.mapper.SessionMapper;
import com.bootcamp.movie2gether.movie.service.SessionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@ResponseBody
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionMapper sessionMapper;

    @GetMapping()
    Page<SessionDetail> getSessionOverviews(@RequestParam Integer page,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) String movieId,
                                            @RequestParam(required = false) String cinemaId) {
        return sessionService.getDetailsPaged(page, pageSize, cinemaId == null ? null : new ObjectId(cinemaId), movieId == null ? null : new ObjectId(movieId));
    }

}
