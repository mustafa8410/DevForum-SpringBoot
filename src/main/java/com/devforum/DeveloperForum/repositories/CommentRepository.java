package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByUserId(Long userId, Pageable pageable);
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
    Page<Comment> findAllByPostIdOrderByCommentDateDesc(Long postId, Pageable pageable);
    Page<Comment> findAllByPostIdOrderByNumberOfReactionsDescCommentDateDesc(Long postId, Pageable pageable);
    Page<Comment> findAllByUserIdOrderByCommentDateDesc(Long userId, Pageable pageable);
    Page<Comment> findAllByUserIdOrderByNumberOfReactionsDescCommentDateDesc(Long userId, Pageable pageable);
    Page<Comment> findAllByOrderByNumberOfReactionsDescCommentDateDesc(Pageable pageable);
    Page<Comment> findAllByPostIdAndUserIdOrderByNumberOfReactionsDescCommentDateDesc(Long postId, Long userId,
                                                                                      Pageable pageable);
    Page<Comment> findAllByPostIdAndUserIdOrderByCommentDateDesc(Long postId, Long userId, Pageable pageable);
    Page<Comment> findAllByPostIdAndUserId(Long postId, Long userId, Pageable pageable);
    @Query("SELECT c FROM Comment c WHERE c.commentDate >= :oneWeekAgo ORDER BY c.numberOfReactions DESC, c.commentDate DESC")
    Page<Comment> findTopCommentsWithinWeek(@Param("oneWeekAgo") Date oneWeekAgo, Pageable pageable);
}
