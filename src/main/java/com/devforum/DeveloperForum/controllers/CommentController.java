package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.requests.CommentRequests.CommentCreateRequest;
import com.devforum.DeveloperForum.requests.CommentRequests.CommentUpdateRequest;
import com.devforum.DeveloperForum.responses.CommentResponse;
import com.devforum.DeveloperForum.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Page<CommentResponse> getAllComments(@RequestParam Optional<Long> postId,
                                                @RequestParam Optional<Long> userId,
                                                @RequestParam Optional<String> sortBy,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int pageSize) {
        return commentService.getAllComments(postId, userId, sortBy, page, pageSize);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.FOUND)
    public CommentResponse findCommentById(@PathVariable Long commentId){
        return commentService.findCommentById(commentId);
    }

    @GetMapping("/top")
    @ResponseStatus(HttpStatus.FOUND)
    public Page<CommentResponse> findTopCommentsWithinWeek(@RequestParam Optional<Long> postId,
                                                           @RequestParam Optional<Long> userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize){
        return commentService.findTopCommentsWithinWeek(postId, userId, page, pageSize);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody CommentCreateRequest commentCreateRequest){
        return commentService.createComment(commentCreateRequest);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Comment updateCommentById(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest){
        return commentService.updateCommentById(commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
    }
}
