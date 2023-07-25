package com.mysns.api.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mysns.api.domain.Post;
import com.mysns.api.exception.PostNotFoundException;
import com.mysns.api.repository.PostRepository;
import com.mysns.api.request.PostCreateRequest;
import com.mysns.api.request.PostSearchRequest;
import com.mysns.api.request.PostUpdateRequest;
import com.mysns.api.response.PostResponse;
import java.util.List;
import java.util.stream.IntStream;
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
    @DisplayName("post를 생성할 수 있다")
    void createPost() {
        //given
        PostCreateRequest postRequest = PostCreateRequest.builder()
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
    @DisplayName("id로 post 1개를 조회할 수 있다")
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

    @Test
    @DisplayName("invalid: id로 post 1개를 조회할 수 있다")
    void invalid_findPost() {
        //given
        Post post = postRepository.save(Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build());

        //when then
        assertThatThrownBy(() -> postService.findPost(post.getId() + 1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("post 여러 개를 조회할 수 있다")
    void findPosts() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목" + i)
                                .content("본문" + i)
                                .build())
                .toList();
        postRepository.saveAll(requestPosts);
        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .size(10)
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.findPosts(postSearchRequest);

        //then
        assertThat(posts.size()).isEqualTo(postSearchRequest.getSize());
        assertThat(posts.get(0).getTitle()).isEqualTo(requestPosts.get(19).getTitle());
    }

    @Test
    @DisplayName("post를 수정할 수 있다")
    void updatePost() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title("변경된 제목")
                .content("변경된 본문")
                .build();

        //when
        postService.updatePost(savedPost.getId(), postUpdateRequest);

        //then
        Post updatedPost = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + savedPost.getId()));
        assertThat(updatedPost.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(postUpdateRequest.getContent());
    }

    @Test
    @DisplayName("invalid: post를 수정할 수 있다")
    void invlid_updatePost() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title("변경된 제목")
                .content("변경된 본문")
                .build();

        //when then
        assertThatThrownBy(() -> postService.updatePost(savedPost.getId() + 1, postUpdateRequest))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("post를 삭제할 수 있다")
    void deletePost() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());

        //when
        postService.deletePost(savedPost.getId());

        //then
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("invalid: post를 삭제할 수 있다")
    void invalid_deletePost() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());

        //when then
        assertThatThrownBy(() -> postService.deletePost(savedPost.getId() + 1))
                .isInstanceOf(PostNotFoundException.class);
    }
}
