package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.entities.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    public boolean existsByReactorIdAndComment(Long reactorId, Comment comment);
}
