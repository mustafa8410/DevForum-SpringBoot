package com.devforum.DeveloperForum.security;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${devforum.secret}")
    private String SECRET_KEY;

    @Value("${token.expires.in}")
    private Long VALIDITY;

    public String generateToken(Authentication authentication){
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("cred", userDetails.getPassword());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(VALIDITY)))
                .signWith(generateKey())
                .compact();

    }
    private SecretKey generateKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private Claims getPayload(String token){
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token){
        return getPayload(token).getSubject();
    }

    public String extractPassword(String token){
        return getPayload(token).get("cred").toString();
    }

    public boolean isTokenExpired(String token){
        return getPayload(token).getExpiration().before(Date.from(Instant.now()));
    }

    public boolean verifyUserWithToken(Optional<User> user, String token){
        if(user.isEmpty())
            throw new UserNotFoundException("User doesn't exist.");
        Claims claims = getPayload(token);
        return (user.get().getUsername().equals(claims.getSubject()) &&
                user.get().getPassword().equals(claims.get("cred").toString()));
    }

}