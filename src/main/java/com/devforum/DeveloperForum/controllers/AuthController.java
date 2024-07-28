package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.requests.LoginRequest;
import com.devforum.DeveloperForum.requests.RefreshJwtTokenRequest;
import com.devforum.DeveloperForum.responses.AuthResponse;
import com.devforum.DeveloperForum.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public AuthResponse register(@RequestBody CreateUserRequest createUserRequest){
//        if(userRepository.existsByEmail(createUserRequest.getEmail()))
//            throw new EmailAlreadyExistsException("A user with given email already exists.");
//        if(userRepository.existsByUsername(createUserRequest.getUsername()))
//            throw new UsernameAlreadyExistsException("A user with given username already exists.");
//
//    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse refreshJwtToken(@RequestBody RefreshJwtTokenRequest refreshJwtTokenRequest){
        return authService.refreshJwtToken(refreshJwtTokenRequest);
    }

}
