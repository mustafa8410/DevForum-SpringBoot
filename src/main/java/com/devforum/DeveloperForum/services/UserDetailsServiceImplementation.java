package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.NotAuthorizedException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.security.JwtUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null)
            throw new UserNotFoundException("No user with given username is found.");
        return new JwtUserDetails(username, user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("user")));
    }

    public void verifyUser(User userInstance){
        JwtUserDetails userDetails = (JwtUserDetails) loadUserByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        if(userDetails.getUsername().equals(userInstance.getUsername()) && userDetails.getPassword()
                .equals(userInstance.getPassword()))
            return;
        throw new NotAuthorizedException("Client is not authorized for this request.");
    }
}
