package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.entities.Post;
import com.devforum.DeveloperForum.requests.PostRequests.PostCreateRequest;
import com.devforum.DeveloperForum.requests.PostRequests.PostUpdateRequest;
import com.devforum.DeveloperForum.responses.PostPreviewResponse;
import com.devforum.DeveloperForum.responses.PostResponse;
import com.devforum.DeveloperForum.services.PostService;
import org.springframework.data.domain.Page;
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
    public Page<PostPreviewResponse> getAllPosts(@RequestParam Optional<Long> userId,
                                                 @RequestParam Optional<String> sortBy,
                                                 @RequestParam Optional<List<String>> category,
                                                 @RequestParam Optional<List<String>> tag,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int pageSize) {
        return postService.getAllPosts(userId, sortBy, category, tag, page, pageSize);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.FOUND)
    public PostResponse findPostById(@PathVariable Long postId){
        return postService.findPostById(postId);
    }

    @GetMapping("/top")
    @ResponseStatus(HttpStatus.FOUND)
    public Page<PostPreviewResponse> findTopPostsWithinWeek(@RequestParam Optional<String> postCategory){
        return postService.findTopPostsWithinWeek(postCategory);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody PostCreateRequest postRequest){
        return postService.createPost(postRequest);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Post updatePostById(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest){
        return postService.updatePostById(postId, postUpdateRequest);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable Long postId){
        postService.deletePostById(postId);
    }

}
