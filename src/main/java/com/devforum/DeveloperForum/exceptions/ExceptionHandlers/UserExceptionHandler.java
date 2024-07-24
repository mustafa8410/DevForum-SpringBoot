package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.UserExceptions.EmailAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<String> handleUserNotFound(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    private ResponseEntity<String> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectUserDataException.class)
    private ResponseEntity<String> handleIncorrectUserData(IncorrectUserDataException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
