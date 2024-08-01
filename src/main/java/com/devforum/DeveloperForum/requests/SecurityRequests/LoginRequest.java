package com.devforum.DeveloperForum.requests.SecurityRequests;

import lombok.Data;

@Data
public class LoginRequest {
    String loginData; //may be email or username
    String password;
}
