package com.devforum.DeveloperForum.responses;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    Long id;
    Long postId;
    Long userId;
    String text;
    Date commentDate;
}
