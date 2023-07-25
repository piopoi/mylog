package com.mysns.api.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.mysns.api.domain.Post;
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
    @DisplayName("글 작성")
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

    @Test
    @DisplayName("post 여러개 조회")
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
    @DisplayName("post 제목 수정")
    void updateTitle() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title("변경된 제목")
                .content(savedPost.getContent())
                .build();

        //when
        postService.updatePost(savedPost.getId(), postUpdateRequest);

        //then
        Post updatedPost = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + savedPost.getId()));
        assertThat(updatedPost.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(savedPost.getContent());
    }

    @Test
    @DisplayName("post 본문 수정")
    void updateContent() {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title(savedPost.getTitle())
                .content("변경된 본문")
                .build();

        //when
        postService.updatePost(savedPost.getId(), postUpdateRequest);

        //then
        Post updatedPost = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + savedPost.getId()));
        assertThat(updatedPost.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(postUpdateRequest.getContent());
    }

    @Test
    @DisplayName("post 제목/본문 수정")
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
}
