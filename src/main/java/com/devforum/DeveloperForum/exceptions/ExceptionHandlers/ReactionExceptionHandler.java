package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.ReactionExceptions.NotAllowedToReactToSelfException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReactionExceptionHandler {
    @ExceptionHandler(ReactionNotFoundException.class)
    private ResponseEntity<String> handleReactionNotFound(ReactionNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReactionAlreadyExistsException.class)
    private ResponseEntity<String> handleReactionAlreadyExists(ReactionAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAllowedToReactToSelfException.class)
    private ResponseEntity<String> handleNotAllowedToReactToSelf(NotAllowedToReactToSelfException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
