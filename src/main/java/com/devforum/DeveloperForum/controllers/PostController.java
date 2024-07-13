package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.requests.CreatePostRequest;
import com.devforum.DeveloperForum.requests.DeletePostRequest;
import com.devforum.DeveloperForum.requests.UpdatePostRequest;
import com.devforum.DeveloperForum.responses.PostPreviewResponse;
import com.devforum.DeveloperForum.responses.PostResponse;
import com.devforum.DeveloperForum.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    // shows only the previews of the posts, when clicked then the front-end should request the specific post by id
    public List<PostPreviewResponse> getAllPosts(@RequestParam Optional<Long> userId,
                                                 @RequestParam Optional<String> sortBy,
                                                 @RequestParam Optional<List<String>> postCategories,
                                                 @RequestParam Optional<List<String>> postTags) {
        return postService.getAllPosts(userId, sortBy, postCategories, postTags);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.FOUND)
    public PostResponse findPostById(@PathVariable Long postId){
        return postService.findPostById(postId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody CreatePostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Post updatePostById(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest){
        return postService.updatePostById(postId, updatePostRequest);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable Long postId){
        postService.deletePostById(postId);
    }

}
