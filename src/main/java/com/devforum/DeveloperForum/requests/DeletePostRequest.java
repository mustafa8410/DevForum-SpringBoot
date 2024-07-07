package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class DeletePostRequest {
    String email;
    String password;
}
