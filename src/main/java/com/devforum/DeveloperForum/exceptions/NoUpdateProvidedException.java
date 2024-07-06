package com.devforum.DeveloperForum.exceptions;

public class NoUpdateProvidedException extends RuntimeException{
    public NoUpdateProvidedException() {
    }

    public NoUpdateProvidedException(String message) {
        super(message);
    }
}
