package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserIdAndPostId(Long userId, Long postId);
    List<Comment> findAllByUserId(Long userId);
    List<Comment> findAllByPostId(Long postId);
    List<Comment> findAllByPostIdOrderByCommentDate(Long postId);
    List<Comment> findAllByPostIdOrderByNumberOfReactions(Long postId);
    List<Comment> findAllByUserIdOrderByCommentDateDesc(Long userId);
    List<Comment> findAllByUserIdOrderByNumberOfReactionsDesc(Long userId);
}
