package com.bootcamp.movie2gether.user.security.service;

import com.bootcamp.movie2gether.user.dto.RegisterRequest;
import com.bootcamp.movie2gether.user.exceptions.EmptyInputException;
import com.bootcamp.movie2gether.user.exceptions.WeakPasswordException;
import com.bootcamp.movie2gether.user.exceptions.WrongEmailFormatException;
import com.bootcamp.movie2gether.user.repository.UserRepository;
import com.bootcamp.movie2gether.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void should_throw_empty_input_exception_when_validate_register_request_given_empty_username() {
        //given
        RegisterRequest registerRequest = new RegisterRequest("", "test@gmail.com", "123123Aa");

        //when
        assertThrows(
                EmptyInputException.class,
                () -> userService.validateRegistrationRequest(registerRequest),
                "Input cannot be empty"
        );
    }

    @Test
    void should_throw_empty_input_exception_when_validate_register_request_given_empty_email() {
        //given
        RegisterRequest registerRequest = new RegisterRequest("testingAccount", "", "123123Aa");

        //when
        assertThrows(
                EmptyInputException.class,
                () -> userService.validateRegistrationRequest(registerRequest),
                "Input cannot be empty"
        );
    }

    @Test
    void should_throw_empty_input_exception_when_validate_register_request_given_empty_password() {
        //given
        RegisterRequest registerRequest = new RegisterRequest("testingAccount", "test@gmail.com", "");

        //when
        assertThrows(
                EmptyInputException.class,
                () -> userService.validateRegistrationRequest(registerRequest),
                "Input cannot be empty"
        );
    }

    @Test
    void should_throw_weak_password_exception_when_validate_register_request_given_weak_password() {
        //given
        RegisterRequest registerRequest = new RegisterRequest("testingAccount", "test@gmail.com", "123");

        //when
        assertThrows(
                WeakPasswordException.class,
                () -> userService.validateRegistrationRequest(registerRequest),
                "Password strength is not enough"
        )
        ;
    }

    @Test
    void should_throw_wrong_email_format_exception_when_validate_register_request_given_invalid_email() {
        //given
        RegisterRequest registerRequest = new RegisterRequest("testingAccount", "test", "123123Aa");

        //when
        assertThrows(
                WrongEmailFormatException.class,
                () -> userService.validateRegistrationRequest(registerRequest),
                "Email with invalid format provided"
        );
    }
}
