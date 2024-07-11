package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReactionExceptionHandler {
    @ExceptionHandler(ReactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleReactionNotFound(){}

    @ExceptionHandler(ReactionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private void handleReactionAlreadyExists(){}
}
