package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.RefreshToken;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.InvalidTokenProvidedException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.NotAuthorizedException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.RefreshTokenRepository;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.LoginRequest;
import com.devforum.DeveloperForum.requests.RefreshJwtTokenRequest;
import com.devforum.DeveloperForum.responses.AuthResponse;
import com.devforum.DeveloperForum.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder,
                       RefreshTokenRepository refreshTokenRepository, UserDetailsServiceImplementation userDetailsService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String username;
        if(userRepository.existsByEmail(loginRequest.getUsernameOrEmail()))
            username = userRepository.findByEmail(loginRequest.getUsernameOrEmail()).get().getUsername();
        else
            username = loginRequest.getUsernameOrEmail();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(userRepository.findByUsername(username).get().getId(), jwtToken);
    }

    public AuthResponse refreshJwtToken(RefreshJwtTokenRequest refreshJwtTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByUserId(refreshJwtTokenRequest.getUserId());
        User user = userRepository.findById(refreshJwtTokenRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("User with given id doesn't exist.");
        if(!refreshToken.getToken().equals(refreshJwtTokenRequest.getRefreshToken()))
            throw new InvalidTokenProvidedException("Refresh token provided doesn't match" +
                    "with the given user's refresh token.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new NotAuthorizedException("Client is not authorized for this request.");
        return new AuthResponse(refreshJwtTokenRequest.getUserId(), jwtTokenProvider.generateToken(authentication));
    }

    public String checkAndRefreshJwtToken(String jwtToken){
        if(jwtTokenProvider.isTokenExpired(jwtToken)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null)
                throw new NotAuthorizedException("Client is not authorized for this request.");
            jwtTokenProvider.generateToken(authentication);
        }
        return jwtToken;
    }
}