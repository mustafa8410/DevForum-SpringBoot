package com.devforum.DeveloperForum.exceptions.SecurityExceptions;

public class UserAlreadyLoggedInException extends RuntimeException{
    public UserAlreadyLoggedInException() {
    }

    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
