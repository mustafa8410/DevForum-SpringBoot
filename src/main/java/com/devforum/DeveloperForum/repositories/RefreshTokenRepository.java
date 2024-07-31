package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
