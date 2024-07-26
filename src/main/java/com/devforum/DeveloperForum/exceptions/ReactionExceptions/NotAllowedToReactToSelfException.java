package com.devforum.DeveloperForum.exceptions.ReactionExceptions;

public class NotAllowedToReactToSelfException extends RuntimeException{
    public NotAllowedToReactToSelfException() {
    }

    public NotAllowedToReactToSelfException(String message) {
        super(message);
    }
}
