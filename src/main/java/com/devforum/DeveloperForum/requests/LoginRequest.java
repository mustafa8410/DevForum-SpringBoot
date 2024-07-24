package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class LoginRequest {
    String usernameOrEmail;
    String password;
}
