package com.devforum.DeveloperForum.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public enum PostCategory {
    C, CPP, CSHARP, JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, FRONTEND, PHP, OTHER;

    public static Collection<PostCategory> turnStringToCategoryCollection(Optional<List<String>> categories){
        Collection<PostCategory> postCategoryCollection = new ArrayList<>();
        if(categories.isEmpty())
            return postCategoryCollection;
        for(String category: categories.get())
            postCategoryCollection.add(PostCategory.valueOf(category));
        return postCategoryCollection;
    }
}
