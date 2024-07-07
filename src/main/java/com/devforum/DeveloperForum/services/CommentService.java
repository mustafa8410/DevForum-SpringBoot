package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.repositories.CommentRepository;
import com.devforum.DeveloperForum.responses.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

//    public List<CommentResponse> getAllComments(Optional<Long> postId, Optional<Long> userId) {
//    }
}
