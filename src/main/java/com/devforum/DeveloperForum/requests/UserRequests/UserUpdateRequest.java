package com.devforum.DeveloperForum.requests.UserRequests;

import com.devforum.DeveloperForum.entities.User;
import lombok.Data;

@Data
public class UserUpdateRequest {
    String email;
    String username;
    String name;
    String password;

    public UserUpdateRequest(User entity){
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
    }

    public UserUpdateRequest(String email, String name, String username, String password) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public boolean allFieldsEqual(User entity){
        return entity.getUsername().equals(this.username) && entity.getEmail().equals(this.email) &&
                entity.getName().equals(this.name);
    }
}
