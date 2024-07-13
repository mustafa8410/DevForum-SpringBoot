package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId, Pageable pageable);
    List<Comment> findAllByPostId(Long postId, Pageable pageable);
    List<Comment> findAllByPostIdOrderByCommentDateDesc(Long postId, Pageable pageable);
    List<Comment> findAllByPostIdOrderByNumberOfReactionsDescCommentDateDesc(Long postId, Pageable pageable);
    List<Comment> findAllByUserIdOrderByCommentDateDesc(Long userId, Pageable pageable);
    List<Comment> findAllByUserIdOrderByNumberOfReactionsDescCommentDateDesc(Long userId, Pageable pageable);
}
