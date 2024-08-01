package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.enums.ReputationRank;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NoUpdateProvidedException;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NullDataProvidedException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.EmailAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UsernameAlreadyExistsException;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.UserRequests.UserCreateRequest;
import com.devforum.DeveloperForum.requests.UserRequests.UserDeleteRequest;
import com.devforum.DeveloperForum.requests.UserRequests.UserUpdateRequest;
import com.devforum.DeveloperForum.responses.UserResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    private final UserDetailsServiceImplementation userDetailsService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserDetailsServiceImplementation userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponse::new).collect(Collectors.toList());

    }

    public UserResponse findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(UserResponse::new).orElse(null);
    }

    public User createUser(UserCreateRequest userRequest) {
        if(userRequest.getUsername().isEmpty())
            throw new NullDataProvidedException("Username can't be empty!");
        if(userRequest.getEmail().isEmpty())
            throw new NullDataProvidedException("All users must have an e-mail.");
        if(userRequest.getPassword().isEmpty())
            throw new NullDataProvidedException("All users must have a password!");
        if(userRequest.getName().isEmpty())
            throw new NullDataProvidedException("All users must provide a name!");
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
        newUser.setHelpfulRank(null);
        return userRepository.save(newUser);
    }

    public User updateUserById(Long userId, UserUpdateRequest userRequest) {
        if(userRequest.getUsername().isEmpty())
            throw new NullDataProvidedException("Username can't be empty!");
        if(userRequest.getEmail().isEmpty())
            throw new NullDataProvidedException("All users must have an e-mail.");
        if(userRequest.getPassword().isEmpty())
            throw new NullDataProvidedException("All users must have a password!");
        if(userRequest.getName().isEmpty())
            throw new NullDataProvidedException("All users must provide a name!");

        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new UserNotFoundException("The user that you're trying to update doesn't exist.");
        }

        userDetailsService.verifyUser(user);


        if(userRequest.allFieldsEqual(user) && BCrypt
                .checkpw(userRequest.getPassword(), user.getPassword()))
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

        if(!BCrypt.checkpw(userRequest.getPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if(!userRequest.getName().equals(user.getName())){
            user.setName(userRequest.getName());
        }

        return userRepository.save(user);
    }

    public void deleteUserById(Long userId, UserDeleteRequest userDeleteRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new UserNotFoundException("There's no user with given id to delete.");
        userDetailsService.verifyUser(user);
        if(!BCrypt.checkpw(userDeleteRequest.getPassword(), user.getPassword()))
            throw new IncorrectUserDataException("Password provided is incorrect.");
        if(!user.getEmail().equals(userDeleteRequest.getEmail()))
            throw new IncorrectUserDataException("Email provided is incorrect.");
        userRepository.deleteById(userId);

    }
}
