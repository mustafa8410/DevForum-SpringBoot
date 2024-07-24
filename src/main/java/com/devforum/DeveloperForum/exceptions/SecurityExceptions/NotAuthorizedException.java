package com.devforum.DeveloperForum.exceptions.SecurityExceptions;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
