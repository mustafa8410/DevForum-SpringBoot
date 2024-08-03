package com.devforum.DeveloperForum.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public enum PostTag {
    QUESTION, MEME, HELPFUL_INFO, DISCUSSION, NONE;

    public static Collection<PostTag> turnStringToTagCollection(Optional<List<String>> tags){
        Collection<PostTag> postTagCollection = new ArrayList<>();
        if(tags.isEmpty())
            return postTagCollection;
        for(String tag: tags.get())
            postTagCollection.add(PostTag.valueOf(tag));
        return postTagCollection;
    }
}
