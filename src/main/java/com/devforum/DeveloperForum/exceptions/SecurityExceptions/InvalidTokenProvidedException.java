package com.devforum.DeveloperForum.exceptions.SecurityExceptions;

public class InvalidTokenProvidedException extends RuntimeException{
    public InvalidTokenProvidedException() {
    }

    public InvalidTokenProvidedException(String message) {
        super(message);
    }
}
