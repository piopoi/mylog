package com.mysns.api.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.mysns.api.domain.Post;
import com.mysns.api.repository.PostRepository;
import com.mysns.api.request.PostRequest;
import com.mysns.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void savePost() {
        //given
        PostRequest postRequest = PostRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.savePost(postRequest);

        //then
        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertThat(post.title).isEqualTo("제목입니다.");
        assertThat(post.content).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("postId로 Post 1개를 조회할 수 있다.")
    void findPost() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build());

        //when
        PostResponse postResponse = postService.findPost(savedPost.getId());

        //then
        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getId()).isEqualTo(savedPost.getId());
        assertThat(postResponse.getTitle()).isEqualTo("제목입니다.");
        assertThat(postResponse.getContent()).isEqualTo("내용입니다.");
    }
}
