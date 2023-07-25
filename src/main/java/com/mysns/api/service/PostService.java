package com.mysns.api.service;

import com.mysns.api.domain.Post;
import com.mysns.api.exception.PostNotFoundException;
import com.mysns.api.repository.PostRepository;
import com.mysns.api.request.PostCreateRequest;
import com.mysns.api.request.PostSearchRequest;
import com.mysns.api.request.PostUpdateRequest;
import com.mysns.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void savePost(PostCreateRequest postCreateRequest) {
        Post post = postCreateRequest.toPost();
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.of(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findPosts(PostSearchRequest postSearchRequest) {
        return postRepository.getList(postSearchRequest)
                .stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePost(Long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.update(postUpdateRequest.toPost());
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        postRepository.deleteById(id);
    }
}
