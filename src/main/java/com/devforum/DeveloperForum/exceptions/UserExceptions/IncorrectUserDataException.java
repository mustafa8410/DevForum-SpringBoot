package com.devforum.DeveloperForum.exceptions.UserExceptions;

public class IncorrectUserDataException extends RuntimeException{
    public IncorrectUserDataException() {
    }

    public IncorrectUserDataException(String message) {
        super(message);
    }
}
