package com.devforum.DeveloperForum.requests;

import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import lombok.Data;

import java.util.Optional;

@Data
public class CreatePostRequest {
    Long userId;
    String title;
    String text;
    String postCategory;
    String postTag;
}
