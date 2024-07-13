package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.requests.CreateCommentRequest;
import com.devforum.DeveloperForum.requests.UpdateCommentRequest;
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

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> postId,
                                                @RequestParam Optional<Long> userId,
                                                @RequestParam Optional<String> sortBy) {
        return commentService.getAllComments(postId, userId, sortBy);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.FOUND)
    public CommentResponse findCommentById(@PathVariable Long commentId){
        return commentService.findCommentById(commentId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody CreateCommentRequest createCommentRequest){
        return commentService.createComment(createCommentRequest);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Comment updateCommentById(@PathVariable Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest){
        return commentService.updateCommentById(commentId, updateCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
    }
}
