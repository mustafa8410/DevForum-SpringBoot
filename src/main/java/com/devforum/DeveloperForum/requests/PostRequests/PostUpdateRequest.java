package com.devforum.DeveloperForum.requests.PostRequests;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PostUpdateRequest {
    String title;
    String text;
    String postCategory;
    String postTag;

    public boolean allFieldsEqual(Post entity){
        if(StringUtils.hasText(postTag))
            return PostTag.valueOf(this.postTag).equals(entity.getPostTag()) &&
                    PostCategory.valueOf(this.postCategory).equals(entity.getPostCategory()) &&
                    this.title.equals(entity.getTitle()) && this.text.equals(entity.getText());
        else
            return entity.getPostTag() == null &&
                    PostCategory.valueOf(this.postCategory).equals(entity.getPostCategory()) &&
                    this.title.equals(entity.getTitle()) && this.text.equals(entity.getText());
    }

}

