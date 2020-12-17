package com.bootcamp.movie2gether.movie.controller;

import com.bootcamp.movie2gether.movie.dto.SessionDetail;
import com.bootcamp.movie2gether.movie.mapper.SessionMapper;
import com.bootcamp.movie2gether.movie.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@ResponseBody
@CrossOrigin
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionMapper sessionMapper;

    @GetMapping(produces = {"application/json"})
    @ApiResponse(description = "Success", responseCode = "200")
    @Operation(summary = "Get movie sessions details filtered by movie AND/OR cinema.")
    Page<SessionDetail> getSessionOverviews(@RequestParam Integer page,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) String movieId,
                                            @RequestParam(required = false) String cinemaId) {
        return sessionService.getDetailsPaged(page, pageSize, cinemaId == null ? null : new ObjectId(cinemaId), movieId == null ? null : new ObjectId(movieId));
    }

    @GetMapping(value = "/{sessionId}", produces = {"application/json"})
    @Operation(summary = "Get movie sessions detail by id.")
    SessionDetail getSessionById(@PathVariable String sessionId) {
        return sessionService.getSessionById(new ObjectId(sessionId));
    }

}
