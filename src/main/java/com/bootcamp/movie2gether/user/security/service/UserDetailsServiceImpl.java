package com.bootcamp.movie2gether.user.security.service;

import com.bootcamp.movie2gether.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UserRepository userRepository;

}
