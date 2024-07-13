package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.InvalidQueryStatementProvidedException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.CommentRepository;
import com.devforum.DeveloperForum.repositories.PostRepository;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.CreateCommentRequest;
import com.devforum.DeveloperForum.requests.UpdateCommentRequest;
import com.devforum.DeveloperForum.responses.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<CommentResponse> getAllComments(Optional<Long> postId, Optional<Long> userId, Optional<String> sortBy){
        List<Comment> commentList;
        if(postId.isEmpty() && userId.isEmpty())
            throw new InvalidQueryStatementProvidedException
                    ("One query statement must be provided for this method to execute.");
        else if(postId.isPresent() && userId.isPresent())
            throw new InvalidQueryStatementProvidedException
                    ("Only post id or user id must be provided in the query statement.");
        else if(postId.isPresent()){
            Post post = postRepository.findById(postId.get()).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            if(sortBy.isEmpty() || sortBy.get().equals("oldest"))
                commentList = commentRepository.findAllByPostId(postId.get());
            else if(sortBy.get().equals("most_recent"))
                commentList = commentRepository.findAllByPostIdOrderByCommentDate(postId.get());
            else if(sortBy.get().equals("popularity"))
                commentList = commentRepository.findAllByPostIdOrderByNumberOfReactions(postId.get());
            else
                throw new IllegalArgumentException();
        }
        else{
            User commenter = userRepository.findById(userId.get()).orElse(null);
            if(commenter == null)
                throw new UserNotFoundException("User not found.");
            if(sortBy.isEmpty() || sortBy.get().equals("oldest"))
                commentList = commentRepository.findAllByUserId(userId.get());
            else if(sortBy.get().equals("most_recent"))
                commentList = commentRepository.findAllByUserIdOrderByCommentDateDesc(userId.get());
            else if(sortBy.get().equals("popularity"))
                commentList = commentRepository.findAllByUserIdOrderByNumberOfReactionsDesc(userId.get());
            else
                throw new IllegalArgumentException();
        }
        if(commentList.isEmpty())
            throw new CommentNotFoundException("No comment found.");
        return commentList.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    public CommentResponse findCommentById(Long commentId) {
        Comment entity = commentRepository.findById(commentId).orElse(null);
        if(entity == null)
            throw new CommentNotFoundException("No comment found.");
        return new CommentResponse(entity);
    }

    public Comment createComment(CreateCommentRequest createCommentRequest) {
        Post post = postRepository.findById(createCommentRequest.getPostId()).orElse(null);
        if(post == null)
            throw new PostNotFoundException("No post found.");
        User user = userRepository.findById(createCommentRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("No user found.");
        Comment newComment = new Comment();
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setText(createCommentRequest.getText());
        newComment.setCommentDate(new Date());
        newComment.setNumberOfReactions(0L);
        return commentRepository.save(newComment);
    }

    public Comment updateCommentById(Long commentId, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            throw new CommentNotFoundException("There's no comment with given id to update.");
        comment.setText(updateCommentRequest.getText());
        return commentRepository.save(comment);
    }

    public void deleteCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            throw new CommentNotFoundException("The wanted comment to delete couldn't be found.");
        commentRepository.delete(comment);
    }
}
