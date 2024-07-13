package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);
    List<Post> findAllByOrderByNumberOfReactionsDesc();
    List<Post> findAllByUserIdOrderByNumberOfReactionsDesc(Long userId);
    List<Post> findAllByOrderByPostDateDesc();
    List<Post> findAllByUserIdOrderByPostDateDesc(Long userId);
    List<Post> findAllByPostCategoryInAndPostTagIn(Collection<PostCategory> postCategories,
                                                   Collection<PostTag> postTags);
    List<Post> findAllByPostCategoryIn(Collection<PostCategory> postCategories);
    List<Post> findAllByPostTagIn(Collection<PostTag> postTags);
    List<Post> findAllByUserIdAndPostCategoryInAndPostTagIn(Long userId, Collection<PostCategory> postCategories,
                                                            Collection<PostTag> postTags);
    List<Post> findAllByUserIdAndPostCategoryIn(Long userId, Collection<PostCategory> postCategories);
    List<Post> findAllByUserIdAndPostTagIn(Long userId, Collection<PostTag> postTags);
    List<Post> findAllByPostCategoryInAndPostTagInOrderByNumberOfReactionsDesc(Collection<PostCategory> postCategories,
                                                                               Collection<PostTag> postTags);
    List<Post> findAllByPostCategoryInOrderByNumberOfReactionsDesc(Collection<PostCategory> postCategories);
    List<Post> findAllByPostTagInOrderByNumberOfReactionsDesc(Collection<PostTag> postTags);
    List<Post> findAllByUserIdAndPostCategoryInAndPostTagInOrderByNumberOfReactionsDesc(Long userId, Collection<PostCategory> postCategories,
                                                            Collection<PostTag> postTags);
    List<Post> findAllByUserIdAndPostCategoryInOrderByNumberOfReactionsDesc(Long userId,
                                                                            Collection<PostCategory> postCategories);
    List<Post> findAllByUserIdAndPostTagInOrderByNumberOfReactionsDesc(Long userId, Collection<PostTag> postTags);

                                                              ;

}
