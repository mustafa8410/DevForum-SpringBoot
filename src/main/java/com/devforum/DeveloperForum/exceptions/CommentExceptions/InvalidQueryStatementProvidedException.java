package com.devforum.DeveloperForum.exceptions.CommentExceptions;

public class InvalidQueryStatementProvidedException extends RuntimeException{

    public InvalidQueryStatementProvidedException() {
    }

    public InvalidQueryStatementProvidedException(String message) {
        super(message);
    }
}
