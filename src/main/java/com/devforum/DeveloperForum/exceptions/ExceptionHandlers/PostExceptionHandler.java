package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    private ResponseEntity<String> handlePostNotFound(PostNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
