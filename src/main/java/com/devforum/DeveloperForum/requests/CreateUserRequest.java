package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class CreateUserRequest {

    Long id;
    String email;
    String username;
    String name;
    String password;

}
