package com.devforum.DeveloperForum.responses;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import lombok.Data;

import java.util.Date;

@Data
public class PostPreviewResponse {
    Long id;
    Long userId;
    String title;
    PostCategory postCategory;
    PostTag postTag;
    Date postDate;

    public PostPreviewResponse(Post entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.title = entity.getTitle();
        this.postCategory = entity.getPostCategory();
        this.postTag = entity.getPostTag();
        this.postDate = entity.getPostDate();
    }
}


