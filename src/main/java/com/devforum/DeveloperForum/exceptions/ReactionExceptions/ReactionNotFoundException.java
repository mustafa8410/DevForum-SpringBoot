package com.devforum.DeveloperForum.exceptions.ReactionExceptions;

public class ReactionNotFoundException extends RuntimeException{
    public ReactionNotFoundException() {
    }

    public ReactionNotFoundException(String message) {
        super(message);
    }
}
