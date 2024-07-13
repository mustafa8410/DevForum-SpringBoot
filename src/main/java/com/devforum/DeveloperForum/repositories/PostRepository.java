package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId, Pageable pageable);
    List<Post> findAllByOrderByNumberOfReactionsDesc(Pageable pageable);
    List<Post> findAllByUserIdOrderByNumberOfReactionsDesc(Long userId, Pageable pageable);
    List<Post> findAllByOrderByPostDateDesc(Pageable pageable);
    List<Post> findAllByUserIdOrderByPostDateDesc(Long userId, Pageable pageable);
    List<Post> findAllByPostCategoryInAndPostTagIn(Collection<PostCategory> postCategories,
                                                   Collection<PostTag> postTags, Pageable pageable);
    List<Post> findAllByPostCategoryIn(Collection<PostCategory> postCategories, Pageable pageable);
    List<Post> findAllByPostTagIn(Collection<PostTag> postTags, Pageable pageable);
    List<Post> findAllByUserIdAndPostCategoryInAndPostTagIn(Long userId, Collection<PostCategory> postCategories,
                                                            Collection<PostTag> postTags, Pageable pageable);
    List<Post> findAllByUserIdAndPostCategoryIn(Long userId, Collection<PostCategory> postCategories,
                                                Pageable pageable);
    List<Post> findAllByUserIdAndPostTagIn(Long userId, Collection<PostTag> postTags, Pageable pageable);
    List<Post> findAllByPostCategoryInAndPostTagInOrderByNumberOfReactionsDesc(Collection<PostCategory> postCategories,
                                                                               Collection<PostTag> postTags,
                                                                               Pageable pageable);
    List<Post> findAllByPostCategoryInOrderByNumberOfReactionsDesc(Collection<PostCategory> postCategories,
                                                                   Pageable pageable);
    List<Post> findAllByPostTagInOrderByNumberOfReactionsDesc(Collection<PostTag> postTags, Pageable pageable);
    List<Post> findAllByUserIdAndPostCategoryInAndPostTagInOrderByNumberOfReactionsDesc(Long userId,
                                                                                        Collection<PostCategory>
                                                                                                postCategories,
                                                                                        Collection<PostTag> postTags,
                                                                                        Pageable pageable);
    List<Post> findAllByUserIdAndPostCategoryInOrderByNumberOfReactionsDesc(Long userId,
                                                                            Collection<PostCategory> postCategories,
                                                                            Pageable pageable);
    List<Post> findAllByUserIdAndPostTagInOrderByNumberOfReactionsDesc(Long userId, Collection<PostTag> postTags,
                                                                       Pageable pageable);

                                                              ;

}
