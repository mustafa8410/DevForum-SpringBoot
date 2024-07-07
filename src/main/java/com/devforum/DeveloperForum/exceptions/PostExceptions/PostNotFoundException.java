package com.devforum.DeveloperForum.exceptions.PostExceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
