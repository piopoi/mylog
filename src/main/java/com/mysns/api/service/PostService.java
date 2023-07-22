package com.mysns.api.service;

import com.mysns.api.domain.Post;
import com.mysns.api.repository.PostRepository;
import com.mysns.api.request.PostRequest;
import com.mysns.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void savePost(PostRequest postRequest) {
        Post post = postRequest.toPost();
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return PostResponse.of(post);
    }

    public List<PostResponse> findPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }
}
