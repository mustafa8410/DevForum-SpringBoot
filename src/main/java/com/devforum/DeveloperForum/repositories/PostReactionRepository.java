package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.PostReaction;
import com.devforum.DeveloperForum.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    public boolean existsByReactorIdAndPost(Long reactorId, Post post);

    public List<PostReaction> findAllByPostId(Long postId);

    public List<PostReaction> findAllByPostIdAndReactionType(Long postId, ReactionType reactionType);

    public Optional<PostReaction> findByReactorIdAndPost(Long reactorId, Post post);

}
