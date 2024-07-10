package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.Comment;
import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    Long id;
    Long postId;
    Long userId;
    String text;
    Date commentDate;

    public CommentResponse(Comment entity){
        this.id = entity.getId();
        this.postId = entity.getPost().getId();
        this.userId = entity.getUser().getId();
        this.text = entity.getText();
        this.commentDate = entity.getCommentDate();
    }
}
