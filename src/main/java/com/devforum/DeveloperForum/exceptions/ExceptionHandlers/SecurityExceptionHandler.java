package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.SecurityExceptions.InvalidTokenProvidedException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.NotAuthorizedException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.RefreshTokenExpiredException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.RefreshTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRefreshTokenNotFound(){}

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleRefreshExpired(RefreshTokenExpiredException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenProvidedException.class)
    public ResponseEntity<String> handleInvalidTokenProvided(IllegalArgumentException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorized(NotAuthorizedException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
