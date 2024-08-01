package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.RefreshToken;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.InvalidTokenProvidedException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.NotAuthorizedException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.RefreshTokenExpiredException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.SecurityRequests.LoginRequest;
import com.devforum.DeveloperForum.requests.SecurityRequests.RefreshJwtTokenRequest;
import com.devforum.DeveloperForum.responses.AuthResponse;
import com.devforum.DeveloperForum.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, UserDetailsServiceImplementation userDetailsService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String username;
        if(userRepository.existsByEmail(loginRequest.getLoginData()))
            username = userRepository.findByEmail(loginRequest.getLoginData()).get().getUsername();
        else
            username = loginRequest.getLoginData();
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null)
            throw new UserNotFoundException("No user with given details exists.");
        if(!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword()))
            throw new IncorrectUserDataException("Provided password is incorrect.");
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.
                generateRefreshToken(userRepository.findByUsername(username).get());
        return new AuthResponse(userRepository.findByUsername(username).get().getId(), jwtToken,
                refreshToken.getToken());
    }

    public AuthResponse refreshJwtToken(RefreshJwtTokenRequest refreshJwtTokenRequest) {
        User user = userRepository.findById(refreshJwtTokenRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("User with given id doesn't exist.");
        RefreshToken refreshToken = refreshTokenService.findByUserId(refreshJwtTokenRequest.getUserId());
        if(!refreshToken.getToken().equals(refreshJwtTokenRequest.getRefreshToken()))
            throw new InvalidTokenProvidedException("Refresh token provided doesn't match" +
                    "with the given user's refresh token.");
        if(refreshTokenService.isRefreshTokenExpired(refreshToken))
            throw new RefreshTokenExpiredException("Please log in to send further requests.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new NotAuthorizedException("Client is not authorized for this request.");
        return new AuthResponse(refreshJwtTokenRequest.getUserId(), jwtTokenProvider.generateToken(authentication),
                refreshToken.getToken());
    }

//    public String checkAndRefreshJwtToken(String jwtToken){
//        if(jwtTokenProvider.isTokenExpired(jwtToken)){
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if(authentication == null)
//                throw new NotAuthorizedException("Client is not authorized for this request.");
//            User user = userRepository.findByUsername(jwtTokenProvider.extractUsername(jwtToken)).orElse(null);
//            if(user == null)
//                throw new UserNotFoundException("This token shouldn't belong to any user.");
//            RefreshToken refreshToken = refreshTokenService.findByUserId(user.getId());
//            return refreshJwtToken(new RefreshJwtTokenRequest(user.getId(), refreshToken.getToken())).getJwtToken();
//        }
//        return jwtToken;
//    }
}
