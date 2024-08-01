package com.devforum.DeveloperForum.requests.UserRequests;

import lombok.Data;

@Data
public class UserCreateRequest {

    String email;
    String username;
    String name;
    String password;

}
