package com.mysns.api.controller;

import com.mysns.api.request.PostCreateRequest;
import com.mysns.api.request.PostSearchRequest;
import com.mysns.api.request.PostUpdateRequest;
import com.mysns.api.response.PostResponse;
import com.mysns.api.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public void createPost(@RequestBody @Valid PostCreateRequest postCreateRequest) {
        postCreateRequest.validate();
        postService.savePost(postCreateRequest);
    }

    @GetMapping("/post/{postId}")
    public PostResponse findPost(@PathVariable Long postId) {
        return postService.findPost(postId);
    }

    @GetMapping("/post")
    public List<PostResponse> findPosts(@ModelAttribute PostSearchRequest postSearchRequest) {
        return postService.findPosts(postSearchRequest);
    }

    @PutMapping("/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                           @RequestBody @Valid PostUpdateRequest postUpdateRequest) {
        postService.updatePost(postId, postUpdateRequest);
    }

    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
}
