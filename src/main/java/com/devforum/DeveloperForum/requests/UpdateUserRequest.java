package com.devforum.DeveloperForum.requests;

import com.devforum.DeveloperForum.entities.User;
import lombok.Data;

@Data
public class UpdateUserRequest {
    String email;
    String username;
    String name;
    String password;

    public UpdateUserRequest(User entity){
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
    }

    public UpdateUserRequest(String email, String name, String username, String password) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public boolean allFieldsEqual(User entity, UpdateUserRequest request){
        return entity.getUsername().equals(request.getUsername()) && entity.getEmail().equals(request.getEmail()) &&
                entity.getName().equals(request.getName());
    }
}
