package com.devforum.DeveloperForum.exceptions.SecurityExceptions;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException() {
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
