package com.bootcamp.movie2gether.user.service;

import com.bootcamp.movie2gether.user.dto.RegisterRequest;
import com.bootcamp.movie2gether.user.exceptions.EmptyInputException;
import com.bootcamp.movie2gether.user.exceptions.WeakPasswordException;
import com.bootcamp.movie2gether.user.exceptions.WrongEmailFormatException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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

        if (!registerRequest.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")) {
            throw new WeakPasswordException();
        }

    }
}
