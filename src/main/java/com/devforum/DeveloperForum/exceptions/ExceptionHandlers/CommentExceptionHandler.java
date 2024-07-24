package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.InvalidQueryStatementProvidedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommentExceptionHandler {
    @ExceptionHandler(CommentNotFoundException.class)
    private ResponseEntity<String> handleCommentNotFound(CommentNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidQueryStatementProvidedException.class)
    private ResponseEntity<String> handleNoQueryStatementsProvided(InvalidQueryStatementProvidedException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
