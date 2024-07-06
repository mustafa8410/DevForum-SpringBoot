package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.exceptions.UserNotFoundException;
import com.devforum.DeveloperForum.requests.CreateUserRequest;
import com.devforum.DeveloperForum.requests.UpdateUserRequest;
import com.devforum.DeveloperForum.responses.UserResponse;
import com.devforum.DeveloperForum.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public List<UserResponse> getAllUsers(){
        List<UserResponse> userList = userService.getAllUsers();
        if(userList.isEmpty()) throw new UserNotFoundException("There are no users in the database.");
        else return userList;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserResponse findUserById(@PathVariable Long userId){
        UserResponse user = userService.findUserById(userId);
        if(user == null) throw new UserNotFoundException("No user with given userId is found.");
        else return user;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody CreateUserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserById(@PathVariable Long userId, @RequestBody UpdateUserRequest userRequest){
        return userService.updateUserById(userId, userRequest);
    }

}
