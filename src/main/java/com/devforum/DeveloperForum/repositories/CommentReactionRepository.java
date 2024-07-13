package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.entities.CommentReaction;
import com.devforum.DeveloperForum.entities.PostReaction;
import com.devforum.DeveloperForum.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    public boolean existsByReactorIdAndComment(Long reactorId, Comment comment);

    public List<CommentReaction> findAllByCommentId(Long commentId);

    public List<CommentReaction> findAllByCommentIdAndReactionType(Long commentId, ReactionType reactionType);

    public Optional<CommentReaction> findByReactorIdAndComment(Long reactorId, Comment comment);

    public Long countByCommentIdAndReactionType(Long commentId, ReactionType reactionType);
}
