package com.devforum.DeveloperForum.requests.CommentRequests;

import lombok.Data;

@Data
public class CommentCreateRequest {
    Long postId;
    Long userId;
    String text;
}
