package com.devforum.DeveloperForum.exceptions.ReactionExceptions;

public class ReactionAlreadyExistsException extends RuntimeException{
    public ReactionAlreadyExistsException() {
    }

    public ReactionAlreadyExistsException(String message) {
        super(message);
    }
}
