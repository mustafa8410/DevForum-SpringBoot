package com.devforum.DeveloperForum.requests;

import com.devforum.DeveloperForum.entities.User;
import lombok.Data;

@Data
public class UpdateUserRequest {
    String email;
    String name;
    String username;
    String oldPassword;
    String password;

    public UpdateUserRequest(User entity){
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.oldPassword = entity.getPassword();
        this.password = entity.getPassword();
    }

    public UpdateUserRequest(String email, String name, String username, String password) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
