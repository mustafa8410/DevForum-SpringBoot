package com.devforum.DeveloperForum.requests.PostRequests;

import lombok.Data;

@Data
public class PostCreateRequest {
    Long userId;
    String title;
    String text;
    String postCategory;
    String postTag;
}
