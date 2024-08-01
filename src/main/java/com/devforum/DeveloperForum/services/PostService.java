package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NoUpdateProvidedException;
import com.devforum.DeveloperForum.exceptions.GlobalExceptions.NullDataProvidedException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.SecurityExceptions.NotAuthorizedException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.PostRepository;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.PostRequests.PostCreateRequest;
import com.devforum.DeveloperForum.requests.PostRequests.PostUpdateRequest;
import com.devforum.DeveloperForum.responses.PostPreviewResponse;
import com.devforum.DeveloperForum.responses.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final UserDetailsServiceImplementation userDetailsService;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       UserDetailsServiceImplementation userDetailsService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public Page<PostPreviewResponse> getAllPosts(Optional<Long> userId, Optional<String> sortBy,
                                                 Optional<List<String>> postCategories, Optional<List<String>> postTags,
                                                 int page, int pageSize
    /* Filtering by post category and post tag to be done later */
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> postList;
        Collection<PostCategory> postCategoryCollection = PostCategory.turnStringToCategoryCollection(postCategories);
        Collection<PostTag> postTagCollection = PostTag.turnStringToTagCollection(postTags);
        if( (sortBy.isPresent() || postCategories.isPresent() || postTags.isPresent()) &&
                (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser") ||
                        SecurityContextHolder.getContext().getAuthentication() == null) )
            throw new NotAuthorizedException("You must log in to see the posts in detailed filtering.");
        if(sortBy.isEmpty() || sortBy.get().equals("most_recent")){
            if (userId.isEmpty()){
                if(postCategoryCollection.isEmpty() && postTagCollection.isEmpty())
                    postList = postRepository.findAllByOrderByPostDateDesc(pageable);
                else if(!postCategoryCollection.isEmpty() && !postTagCollection.isEmpty()) //only by tag and category
                    postList = postRepository.
                            findAllByPostCategoryInAndPostTagInOrderByPostDateDesc(postCategoryCollection,
                                    postTagCollection, pageable);

                else if(postTagCollection.isEmpty()) //only by category
                    postList = postRepository.findAllByPostCategoryInOrderByPostDateDesc(postCategoryCollection,
                            pageable);
                else // tag
                    postList = postRepository.findAllByPostTagInOrderByPostDateDesc(postTagCollection, pageable);

            }

            else{ //with user id
                if(postCategoryCollection.isEmpty() && postTagCollection.isEmpty())
                    postList = postRepository.findAllByUserIdOrderByPostDateDesc(userId.get(), pageable);
                else if(!postCategoryCollection.isEmpty() && !postTagCollection.isEmpty()) // id, category and tag
                    postList = postRepository.
                            findAllByUserIdAndPostCategoryInAndPostTagInOrderByPostDateDesc(userId.get(),
                                    postCategoryCollection, postTagCollection, pageable);
                else if(postTagCollection.isEmpty()) // only by category and id
                    postList = postRepository.findAllByUserIdAndPostCategoryInOrderByPostDateDesc(userId.get(),
                            postCategoryCollection, pageable);
                else //tag and id
                    postList = postRepository.findAllByUserIdAndPostTagInOrderByPostDateDesc(userId.get(),
                            postTagCollection, pageable);
            }

        }
        else if(sortBy.get().equals("popularity")){
            if(userId.isEmpty()){
                if(postCategoryCollection.isEmpty() && postTagCollection.isEmpty())
                    postList = postRepository.findAllByOrderByNumberOfReactionsDescPostDateDesc(pageable);
                else if(!postCategoryCollection.isEmpty() && !postTagCollection.isEmpty()) //category, tag
                    postList = postRepository
                            .findAllByPostCategoryInAndPostTagInOrderByNumberOfReactionsDescPostDateDesc
                            (postCategoryCollection, postTagCollection, pageable);
                else if(postTagCollection.isEmpty()) // category
                    postList = postRepository.findAllByPostCategoryInOrderByNumberOfReactionsDescPostDateDesc
                            (postCategoryCollection, pageable);
                else  //tag
                    postList = postRepository.findAllByPostTagInOrderByNumberOfReactionsDescPostDateDesc
                            (postTagCollection, pageable);
            }
            else{
                if(postCategoryCollection.isEmpty() && postTagCollection.isEmpty()) //id
                    postList = postRepository.findAllByUserIdOrderByNumberOfReactionsDescPostDateDesc(userId.get(),
                            pageable);
                else if(!postCategoryCollection.isEmpty() && !postTagCollection.isEmpty()) // id, category, tag
                    postList = postRepository.
                            findAllByUserIdAndPostCategoryInAndPostTagInOrderByNumberOfReactionsDescPostDateDesc
                                    (userId.get(), postCategoryCollection, postTagCollection, pageable);
                else if(postTagCollection.isEmpty()) // id, category
                    postList = postRepository.findAllByUserIdAndPostCategoryInOrderByNumberOfReactionsDescPostDateDesc
                            (userId.get(), postCategoryCollection, pageable);
                else //id, tag
                    postList = postRepository.findAllByUserIdAndPostTagInOrderByNumberOfReactionsDescPostDateDesc
                            (userId.get(), postTagCollection, pageable);
            }
        }
        else
            throw new IllegalArgumentException();
        if (postList.isEmpty())
            throw new PostNotFoundException("No post found.");
        return postList.map(PostPreviewResponse::new);
    }

    public PostResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null)
            throw new PostNotFoundException("There's no post with given id.");
        return new PostResponse(post);
    }

    public Page<PostPreviewResponse> findTopPostsWithinWeek(Optional<String> postCategory) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> posts;
        if(postCategory.isEmpty())
            posts = postRepository.findTopPostsWithinWeek(Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        else{
            PostCategory category = PostCategory.valueOf(postCategory.get());
            posts = postRepository.findTopPostsByCategoryWithinWeek(category, Date.from(Instant.now().minusSeconds
                    (60 * 60 * 24 * 7)), pageable);
        }
        if(posts.isEmpty())
            throw new PostNotFoundException("No posts are found that are posted within the last week.");
        return posts.map(PostPreviewResponse::new);
    }


    public Post createPost(PostCreateRequest postRequest) {
        User user = userRepository.findById(postRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("There are no users with given id.");
        userDetailsService.verifyUser(user);
        if(postRequest.getTitle().isEmpty())
            throw new NullDataProvidedException("Every post must have a title.");
        if(postRequest.getPostCategory().isEmpty())
            throw new NullDataProvidedException("Every post must have a category, at least OTHER must be selected.");
        PostCategory postCategory = PostCategory.valueOf(postRequest.getPostCategory().toUpperCase());
        Post newPost = new Post();
        if(StringUtils.hasText(postRequest.getPostTag())){
            PostTag postTag = PostTag.valueOf(postRequest.getPostTag().toUpperCase());
            newPost.setPostTag(postTag);
        }
        else
            newPost.setPostTag(null);
        newPost.setPostDate(new Date());
        newPost.setPostCategory(postCategory);
        newPost.setText(postRequest.getText());
        newPost.setTitle(postRequest.getTitle());
        newPost.setUser(user);
        newPost.setNumberOfReactions(0L);
        return postRepository.save(newPost);
    }

    public Post updatePostById(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            throw new PostNotFoundException("There are no posts with given ID.");
        userDetailsService.verifyUser(post.getUser());
        if(postUpdateRequest.getTitle().isEmpty())
            throw new NullDataProvidedException("Every post must have a title.");
        if(postUpdateRequest.getPostCategory().isEmpty())
            throw new NullDataProvidedException("Every post must have a category, at least OTHER must be selected.");
        if(postUpdateRequest.allFieldsEqual(post))
            throw new NoUpdateProvidedException("No update has been provided to the post.");
        if(StringUtils.hasText(postUpdateRequest.getPostTag())){
            PostTag postTag = PostTag.valueOf(postUpdateRequest.getPostTag().toUpperCase());
            post.setPostTag(postTag);
        }
        else
            post.setPostTag(null);
        PostCategory postCategory = PostCategory.valueOf(postUpdateRequest.getPostCategory().toUpperCase());
        post.setText(postUpdateRequest.getText());
        post.setTitle(postUpdateRequest.getTitle());
        post.setPostCategory(postCategory);
        return postRepository.save(post);
    }

    public void deletePostById(Long postId) {
        Post toDelete = postRepository.findById(postId).orElse(null);
        if(toDelete == null)
            throw new PostNotFoundException("This post doesn't exist already.");
        userDetailsService.verifyUser(toDelete.getUser());
        postRepository.delete(toDelete);
    }
}