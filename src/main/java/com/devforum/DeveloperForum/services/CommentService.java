package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.InvalidQueryStatementProvidedException;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NoUpdateProvidedException;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NullDataProvidedException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.CommentRepository;
import com.devforum.DeveloperForum.repositories.PostRepository;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.CommentRequests.CommentCreateRequest;
import com.devforum.DeveloperForum.requests.CommentRequests.CommentUpdateRequest;
import com.devforum.DeveloperForum.responses.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final UserDetailsServiceImplementation userDetailsService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
                          UserRepository userRepository,
                          UserDetailsServiceImplementation userDetailsService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public Page<CommentResponse> getAllComments(Optional<Long> postId, Optional<Long> userId, Optional<String> sortBy,
                                                int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Comment> commentList;
        if(postId.isEmpty() && userId.isEmpty()){
            if(sortBy.isEmpty() || sortBy.get().equals("popularity")){
                commentList = commentRepository.findAllByOrderByNumberOfReactionsDescCommentDateDesc(pageable);
            }
            else
                throw new InvalidQueryStatementProvidedException
                        ("Only sorting by popularity is allowed in this filtering case.");
        }
        else if(postId.isPresent() && userId.isPresent()){
            Post post = postRepository.findById(postId.get()).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            User commenter = userRepository.findById(userId.get()).orElse(null);
            if(commenter == null)
                throw new UserNotFoundException("User not found.");
            if(sortBy.isEmpty() || sortBy.get().equals("popularity"))
                commentList = commentRepository.findAllByPostIdAndUserIdOrderByNumberOfReactionsDescCommentDateDesc
                        (postId.get(), userId.get(), pageable);
            else if(sortBy.get().equals("most_recent"))
                commentList = commentRepository.findAllByPostIdAndUserIdOrderByCommentDateDesc(postId.get(),
                        userId.get(), pageable);
            else if(sortBy.get().equals("oldest"))
                commentList = commentRepository.findAllByPostIdAndUserId(postId.get(), userId.get(), pageable);
            else
                throw new InvalidQueryStatementProvidedException("Invalid query statement provided.");
        }
        else if(postId.isPresent()){
            Post post = postRepository.findById(postId.get()).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            if(sortBy.isEmpty() || sortBy.get().equals("oldest"))
                commentList = commentRepository.findAllByPostId(postId.get(), pageable);
            else if(sortBy.get().equals("most_recent"))
                commentList = commentRepository.findAllByPostIdOrderByCommentDateDesc(postId.get(), pageable);
            else if(sortBy.get().equals("popularity"))
                commentList = commentRepository.findAllByPostIdOrderByNumberOfReactionsDescCommentDateDesc(postId.get(), pageable);
            else
                throw new InvalidQueryStatementProvidedException("Invalid query statement provided.");
        }
        else{
            User commenter = userRepository.findById(userId.get()).orElse(null);
            if(commenter == null)
                throw new UserNotFoundException("User not found.");
            if(sortBy.isEmpty() || sortBy.get().equals("oldest"))
                commentList = commentRepository.findAllByUserId(userId.get(), pageable);
            else if(sortBy.get().equals("most_recent"))
                commentList = commentRepository.findAllByUserIdOrderByCommentDateDesc(userId.get(), pageable);
            else if(sortBy.get().equals("popularity"))
                commentList = commentRepository.findAllByUserIdOrderByNumberOfReactionsDescCommentDateDesc(userId.get(), pageable);
            else
                throw new InvalidQueryStatementProvidedException("Invalid query statement provided.");
        }
        if(commentList.isEmpty())
            throw new CommentNotFoundException("No comment found.");
        return commentList.map(CommentResponse::new);
    }

    public CommentResponse findCommentById(Long commentId) {
        Comment entity = commentRepository.findById(commentId).orElse(null);
        if(entity == null)
            throw new CommentNotFoundException("No comment found.");
        return new CommentResponse(entity);
    }

    public Page<CommentResponse> findTopCommentsWithinWeek(Optional<Long> postId, Optional<Long> userId,
                                                           int page, int pageSize) {
        Page<Comment> comments;
        if(postId.isEmpty() && userId.isEmpty()) { //general top comments of the week
            Pageable pageable = PageRequest.of(0, 10);
            comments = commentRepository.findTopCommentsWithinWeek(Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        }
        else if(postId.isPresent() && userId.isPresent()){
            Pageable pageable = PageRequest.of(page, pageSize);
            User user = userRepository.findById(userId.get()).orElse(null);
            if(user == null)
                throw new UserNotFoundException("User not found.");
            Post post = postRepository.findById(postId.get()).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            comments = commentRepository.findTopCommentsByUserAndPostWithinWeek(user, post, Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        }
        else if(postId.isPresent()){
            Pageable pageable = PageRequest.of(page, pageSize);
            Post post = postRepository.findById(postId.get()).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            comments = commentRepository.findTopCommentsByPostWithinWeek(post, Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        }
        else {
            Pageable pageable = PageRequest.of(page, pageSize);
            User user = userRepository.findById(userId.get()).orElse(null);
            if(user == null)
                throw new UserNotFoundException("User not found.");
            comments = commentRepository.findTopCommentsByUserWithinWeek(user, Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        }
        if(comments.isEmpty())
            throw new CommentNotFoundException("No comments are found that are posted within the last week.");
        return comments.map(CommentResponse::new);
    }

    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        User user = userRepository.findById(commentCreateRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("No user found.");
        userDetailsService.verifyUser(user);
        Post post = postRepository.findById(commentCreateRequest.getPostId()).orElse(null);
        if(post == null)
            throw new PostNotFoundException("No post found.");
        Comment newComment = new Comment();
        newComment.setPost(post);
        newComment.setUser(user);
        if(commentCreateRequest.getText().isEmpty())
            throw new NullDataProvidedException("A comment can't have an empty text.");
        newComment.setText(commentCreateRequest.getText());
        newComment.setCommentDate(new Date());
        newComment.setNumberOfReactions(0L);
        return commentRepository.save(newComment);
    }

    public Comment updateCommentById(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            throw new CommentNotFoundException("There's no comment with given id to update.");
        if(commentUpdateRequest.getText().equals(comment.getText()))
            throw new NoUpdateProvidedException("No update to the current comment has been provided.");
        if(commentUpdateRequest.getText().isEmpty())
            throw new NullDataProvidedException("A comment can't be blank, you can consider deleting it instead.");
        userDetailsService.verifyUser(comment.getUser());
        comment.setText(commentUpdateRequest.getText());
        return commentRepository.save(comment);
    }

    public void deleteCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            throw new CommentNotFoundException("The wanted comment to delete couldn't be found.");
        userDetailsService.verifyUser(comment.getUser());
        commentRepository.delete(comment);
    }
}
