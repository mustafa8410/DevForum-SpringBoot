package com.devforum.DeveloperForum.exceptions.ExceptionHandlers;

import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NoUpdateProvidedException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.EmailAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NoUpdateProvidedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private void handleNoUpdateProvided(){}

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void handleIllegalArgument(){}

}
