package com.devforum.DeveloperForum.requests;

import lombok.Data;

@Data
public class CreateCommentRequest {
    Long postId;
    Long userId;
    String text;
}
