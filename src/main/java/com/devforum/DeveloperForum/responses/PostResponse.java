package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import lombok.Data;

import java.util.Date;

@Data
public class PostResponse {
    Long id;
    Long userId;
    String title;
    String text;
    PostCategory postCategory;
    PostTag postTag;
    Date postDate;
    Long numberOfReactions;

    public PostResponse(Post entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.postCategory = entity.getPostCategory();
        this.postTag = entity.getPostTag();
        this.postDate = entity.getPostDate();
        this.numberOfReactions = entity.getNumberOfReactions();
    }
}
