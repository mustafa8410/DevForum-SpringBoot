package com.devforum.DeveloperForum.exceptions.SecurityExceptions;

public class RefreshTokenNotFoundException extends RuntimeException{
    public RefreshTokenNotFoundException() {
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
