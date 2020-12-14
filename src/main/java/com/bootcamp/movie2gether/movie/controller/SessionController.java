package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.mapper.SessionMapper;
import com.bootcamp.movie2gether.movie.service.SessionService;
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

    @GetMapping(params = {"page", "pageSize"})
    List<SessionDetail> getSessionOverviews(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return sessionService.getDetailsPaged(page, pageSize);
    }

}