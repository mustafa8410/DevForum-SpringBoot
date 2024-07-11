package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    public boolean existsByReactorIdAndAndPost(Long reactorId, Post post);
}
