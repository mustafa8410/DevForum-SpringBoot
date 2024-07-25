package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class LoginRequest {
    String loginData; //may be email or username
    String password;
}
