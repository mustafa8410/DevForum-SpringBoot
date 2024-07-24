package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.enums.HelpfulRank;
import com.devforum.DeveloperForum.enums.ReputationRank;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NoUpdateProvidedException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.EmailAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UsernameAlreadyExistsException;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.CreateUserRequest;
import com.devforum.DeveloperForum.requests.DeleteUserRequest;
import com.devforum.DeveloperForum.requests.UpdateUserRequest;
import com.devforum.DeveloperForum.responses.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponse::new).collect(Collectors.toList());

    }

    public UserResponse findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(UserResponse::new).orElse(null);
    }

    public User createUser(CreateUserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new EmailAlreadyExistsException("A user with this email already exists.");
        }
        if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new UsernameAlreadyExistsException("A user with this username already exists.");
        }
        User newUser = new User();
        newUser.setEmail(userRequest.getEmail());
        newUser.setName(userRequest.getName());
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setRegisterDate(LocalDate.now());
        newUser.setHelpfulRank(null);
        newUser.setReputationRank(ReputationRank.ROOKIE);
        newUser.setInteractionCount(0L);
        newUser.setHelpfulCount(0L);
        newUser.setHelpfulCount(null);
        return userRepository.save(newUser);
    }

    public User updateUserById(Long userId, UpdateUserRequest userRequest) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new UserNotFoundException("The user that you're trying to update doesn't exist.");
        }

        if(!user.getPassword().equals(userRequest.getOldPassword())){
            throw new IncorrectUserDataException("Current password data provided doesn't match.");
        }
        if(userRequest.equals(new UpdateUserRequest(user)))
            throw new NoUpdateProvidedException("No information about the user is requested to be updated.");

        if(!userRequest.getUsername().equals(user.getUsername())){
            if(userRepository.existsByUsername(userRequest.getUsername())){
                throw new UsernameAlreadyExistsException("There's already a user with this username.");
            }
            user.setUsername(userRequest.getUsername());
        }

        if(!userRequest.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmail(userRequest.getEmail())){
                throw new EmailAlreadyExistsException("There's already a user with this email.");
            }
            user.setEmail(userRequest.getEmail());
        }

        if(!userRequest.getPassword().equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if(!userRequest.getName().equals(user.getName())){
            user.setName(userRequest.getName());
        }

        return userRepository.save(user);
    }

    public void deleteUserById(Long userId, DeleteUserRequest deleteUserRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new UserNotFoundException("There's no user with given id to delete.");
        if(!user.getPassword().equals(deleteUserRequest.getPassword()))
            throw new IncorrectUserDataException("Password provided is incorrect.");
        if(!user.getEmail().equals(deleteUserRequest.getEmail()))
            throw new IncorrectUserDataException("Email provided is incorrect.");
        userRepository.deleteById(userId);

    }
}
