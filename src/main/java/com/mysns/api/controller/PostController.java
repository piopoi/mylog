package com.mysns.api.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.mysns.api.domain.Post;
import com.mysns.api.request.PostRequest;
import com.mysns.api.response.PostResponse;
import com.mysns.api.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    @PostMapping("/post")
    public void createPost(@RequestBody @Valid PostRequest postRequest) {
        postService.savePost(postRequest);
    }

    @GetMapping("/post/{postId}")
    public PostResponse findPost(@PathVariable Long postId) {
        return postService.findPost(postId);
    }

    @GetMapping("/posts")
//    public List<PostResponse> findPosts(@PageableDefault(sort = "id", direction = DESC, size = 5) Pageable pageable) {
    public List<PostResponse> findPosts(@PageableDefault(size = 5)
                                        @SortDefault(sort = "id", direction = DESC)
                                        Pageable pageable) {
        return postService.findPosts(pageable);
    }
}
