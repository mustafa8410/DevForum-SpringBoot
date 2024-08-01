package com.devforum.DeveloperForum.exceptions.GlobalExceptions;

public class NullDataProvidedException extends RuntimeException{
    public NullDataProvidedException() {
    }

    public NullDataProvidedException(String message) {
        super(message);
    }
}
