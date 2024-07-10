package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class UpdatePostRequest {
    String title;
    String text;
    String postCategory;
    String postTag;
}
