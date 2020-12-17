package com.bootcamp.movie2gether.user.service;

import com.bootcamp.movie2gether.user.dto.RegisterRequest;
import com.bootcamp.movie2gether.user.entity.User;
import com.bootcamp.movie2gether.user.exceptions.EmptyInputException;
import com.bootcamp.movie2gether.user.exceptions.UserNotFoundException;
import com.bootcamp.movie2gether.user.exceptions.WeakPasswordException;
import com.bootcamp.movie2gether.user.exceptions.WrongEmailFormatException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import com.bootcamp.movie2gether.user.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void validateRegistrationRequest(RegisterRequest registerRequest) {
        if (registerRequest.getUserName().isEmpty()) {
            throw new EmptyInputException();
        }

        if (registerRequest.getEmail().isEmpty()) {
            throw new EmptyInputException();
        }

        if (registerRequest.getPassword().isEmpty()) {
            throw new EmptyInputException();
        }

        if (!registerRequest.getEmail().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new WrongEmailFormatException();
        }

        if (!registerRequest.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z.!#$%&'*+/=?^_`{|}~-]{8,}$")) {
            throw new WeakPasswordException();
        }

    }

    public UserDetails findUserById(String id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return UserDetailsImpl.build(user);
    }
}
