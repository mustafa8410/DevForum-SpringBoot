package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class DeleteUserRequest {
    String email;
    String password;
}
