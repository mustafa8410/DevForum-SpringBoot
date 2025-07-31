package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.RefreshToken;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.RefreshTokenNotFoundException;
import com.devforum.DeveloperForum.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.expires.in}")
    private Long VALIDITY;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(user, token, Date.from(Instant.now().plusSeconds(VALIDITY)));
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserId(user.getId());
        if (existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();
            existingToken.setExpiryDate(refreshToken.getExpiryDate());
            existingToken.setToken(refreshToken.getToken());
            return refreshTokenRepository.save(existingToken);
        }
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found."));
    }

    public boolean isRefreshTokenExpired(RefreshToken refreshToken){
        return refreshToken.getExpiryDate().before(Date.from(Instant.now()));
    }
}
