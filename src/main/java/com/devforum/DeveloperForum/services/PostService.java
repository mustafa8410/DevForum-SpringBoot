package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.entities.User;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.IncorrectUserDataException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.PostRepository;
import com.devforum.DeveloperForum.repositories.UserRepository;
import com.devforum.DeveloperForum.requests.CreatePostRequest;
import com.devforum.DeveloperForum.requests.DeletePostRequest;
import com.devforum.DeveloperForum.requests.UpdatePostRequest;
import com.devforum.DeveloperForum.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId, Optional<Boolean> mostReaction) {
        List<Post> postList = new ArrayList<>();
        if (userId.isEmpty())
            postList = postRepository.findAll();
        else
            postList = postRepository.findAllByUserId(userId.get());
        if (postList.isEmpty())
            throw new PostNotFoundException("No post found.");
        return postList.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null)
            throw new PostNotFoundException("There's no post with given id.");
        return new PostResponse(post);
    }


    public Post createPost(CreatePostRequest postRequest) {
        User user = userRepository.findById(postRequest.getUserId()).orElse(null);
        if(user == null)
            throw new UserNotFoundException("There are no users with given id.");
        PostCategory postCategory = PostCategory.valueOf(postRequest.getPostCategory().toUpperCase());
        PostTag postTag = PostTag.valueOf(postRequest.getPostTag().toUpperCase());
        Post newPost = new Post();
        newPost.setPostDate(new Date());
        newPost.setPostCategory(postCategory);
        newPost.setPostTag(postTag);
        newPost.setText(postRequest.getText());
        newPost.setTitle(postRequest.getTitle());
        newPost.setUser(user);
        return postRepository.save(newPost);
    }

    public Post updatePostById(Long postId, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            throw new PostNotFoundException("There are no posts with given ID.");
        PostCategory postCategory = PostCategory.valueOf(updatePostRequest.getPostCategory().toUpperCase());
        PostTag postTag = PostTag.valueOf(updatePostRequest.getPostTag().toUpperCase());
        post.setText(updatePostRequest.getText());
        post.setTitle(updatePostRequest.getTitle());
        post.setPostTag(postTag);
        post.setPostCategory(postCategory);
        return postRepository.save(post);
    }

    public void deletePostById(Long postId) {
        Post toDelete = postRepository.findById(postId).orElse(null);
        if(toDelete == null)
            throw new PostNotFoundException("This post doesn't exist already.");
        postRepository.delete(toDelete);
    }
}