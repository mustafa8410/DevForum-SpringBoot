package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.responses.CommentResponse;
import com.devforum.DeveloperForum.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    @GetMapping()
//    @ResponseStatus(HttpStatus.FOUND)
//    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> postId,
//                                                @RequestParam Optional<Long> userId) {
//        return commentService.getAllComments(postId, userId);
//    }
}
