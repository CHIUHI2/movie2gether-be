package com.bootcamp.movie2gether.user.controller;

import com.bootcamp.movie2gether.user.dto.RegisterRequest;
import com.bootcamp.movie2gether.user.dto.RegisterResponse;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.security.config.annotation.web.configuration;
import org.springframework.web.bind.annotation.RestController;

//import static jdk.internal.net.http.AuthenticationFilter.encoder;

@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;

//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        if (userRepository.existsByUserName(registerRequest.getUserName())){
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse());
        }

//        User user = new User(registerRequest.getUserName(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()));
//
//        userRepository.save(user);
        return ResponseEntity.ok(new RegisterResponse());

    }

}
