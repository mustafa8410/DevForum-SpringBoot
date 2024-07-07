package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class UpdatePostRequest {
    Long userId;
    String userPassword;
    String title;
    String text;
    String postCategory;
    String postTag;
}
