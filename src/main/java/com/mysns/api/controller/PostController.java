package com.mysns.api.controller;

import com.mysns.api.request.PostRequest;
import com.mysns.api.response.PostResponse;
import com.mysns.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void createPost(@RequestBody @Valid PostRequest postRequest) {
        postService.savePost(postRequest);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse findPost(@PathVariable(name = "postId") Long id) {
        return postService.findPost(id);
    }
}
