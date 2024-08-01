package com.devforum.DeveloperForum.requests.UserRequests;

import lombok.Data;

@Data
public class UserDeleteRequest {
    String email;
    String password;
}
