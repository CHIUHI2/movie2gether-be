package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.mapper.SessionMapper;
import com.bootcamp.movie2gether.movie.service.SessionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@ResponseBody
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionMapper sessionMapper;

    @GetMapping()
    List<SessionDetail> getSessionOverviews(@RequestParam Integer page,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) ObjectId movieId,
                                            @RequestParam(required = false) ObjectId cinemaId) {
        return sessionService.getDetailsPaged(page, pageSize, cinemaId, movieId);
    }

}
