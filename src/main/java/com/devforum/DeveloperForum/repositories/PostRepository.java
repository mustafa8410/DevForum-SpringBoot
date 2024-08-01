package com.devforum.DeveloperForum.repositories;

import com.devforum.DeveloperForum.entities.Comment;
import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUserIdOrderByPostDateDesc(Long userId, Pageable pageable);
    Page<Post> findAllByOrderByNumberOfReactionsDescPostDateDesc(Pageable pageable);
    Page<Post> findAllByUserIdOrderByNumberOfReactionsDescPostDateDesc(Long userId, Pageable pageable);
    Page<Post> findAllByOrderByPostDateDesc(Pageable pageable);
    Page<Post> findAllByPostCategoryInAndPostTagInOrderByPostDateDesc(Collection<PostCategory> postCategories,
                                                   Collection<PostTag> postTags, Pageable pageable);
    Page<Post> findAllByPostCategoryInOrderByPostDateDesc(Collection<PostCategory> postCategories, Pageable pageable);
    Page<Post> findAllByPostTagInOrderByPostDateDesc(Collection<PostTag> postTags, Pageable pageable);
    Page<Post> findAllByUserIdAndPostCategoryInAndPostTagInOrderByPostDateDesc(Long userId, Collection<PostCategory> postCategories,
                                                            Collection<PostTag> postTags, Pageable pageable);
    Page<Post> findAllByUserIdAndPostCategoryInOrderByPostDateDesc(Long userId, Collection<PostCategory> postCategories,
                                                Pageable pageable);
    Page<Post> findAllByUserIdAndPostTagInOrderByPostDateDesc(Long userId, Collection<PostTag> postTags, Pageable pageable);
    Page<Post> findAllByPostCategoryInAndPostTagInOrderByNumberOfReactionsDescPostDateDesc
            (Collection<PostCategory> postCategories, Collection<PostTag> postTags, Pageable pageable);
    Page<Post> findAllByPostCategoryInOrderByNumberOfReactionsDescPostDateDesc(Collection<PostCategory> postCategories,
                                                                   Pageable pageable);
    Page<Post> findAllByPostTagInOrderByNumberOfReactionsDescPostDateDesc(Collection<PostTag> postTags,
                                                                          Pageable pageable);
    Page<Post> findAllByUserIdAndPostCategoryInAndPostTagInOrderByNumberOfReactionsDescPostDateDesc(Long userId,
                                                                                        Collection<PostCategory>
                                                                                                postCategories,
                                                                                        Collection<PostTag> postTags,
                                                                                        Pageable pageable);
    Page<Post> findAllByUserIdAndPostCategoryInOrderByNumberOfReactionsDescPostDateDesc(Long userId,
                                                                            Collection<PostCategory> postCategories,
                                                                            Pageable pageable);
    Page<Post> findAllByUserIdAndPostTagInOrderByNumberOfReactionsDescPostDateDesc(Long userId,
                                                                                   Collection<PostTag> postTags,
                                                                                   Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postDate >= :oneWeekAgo ORDER BY p.numberOfReactions DESC, p.postDate DESC")
    Page<Post> findTopPostsWithinWeek(@Param("oneWeekAgo") Date oneWeekAgo, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postCategory = :category AND p.postDate >= :oneWeekAgo " +
            "ORDER BY p.numberOfReactions DESC, p.postDate DESC")
    Page<Post> findTopPostsByCategoryWithinWeek(
            @Param("category") PostCategory category,
            @Param("oneWeekAgo") Date oneWeekAgo,
            Pageable pageable);

}
