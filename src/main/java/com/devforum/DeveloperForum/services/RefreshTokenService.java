package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.RefreshToken;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.RefreshTokenNotFoundException;
import com.devforum.DeveloperForum.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.expires.in}")
    private Long VALIDITY;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken(User user){
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(user, token, Date.from(Instant.now().plusSeconds(VALIDITY)));
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByUserId(Long userId){
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElse(null);
        if(refreshToken == null)
            throw new RefreshTokenNotFoundException("Refresh token not found.");
        return refreshToken;
    }
}
