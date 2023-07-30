package com.mysns.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysns.api.domain.Post;
import com.mysns.api.repository.PostRepository;
import com.mysns.api.request.PostCreateRequest;
import com.mysns.api.request.PostUpdateRequest;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("post를 생성할 수 있다")
    void createPost() throws Exception {
        //given
        PostCreateRequest postRequest = PostCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postRequest);

        //when
        mockMvc.perform(post("/post")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertThat(post.title).isEqualTo("제목입니다.");
        assertThat(post.content).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("title 없이 post를 생성할 수 없다")
    void createPost_emptyTitle() throws Exception {
        //given
        PostCreateRequest postRequest = PostCreateRequest.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postRequest);

        //when then
        mockMvc.perform(post("/post")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("title에 욕설을 포함할 수 없다")
    void createPost_invalidTitle() throws Exception {
        //given
        PostCreateRequest postRequest = PostCreateRequest.builder()
                .title("욕설입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postRequest);

        //when then
        mockMvc.perform(post("/post")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("id로 post를 조회할 수 있다")
    void findPost() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        //when then
        mockMvc.perform(get("/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 id로 post를 조회할 수 없다")
    void findPost_notExists() throws Exception {
        mockMvc.perform(get("/post/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("post 여러 개를 조회할 수 있다")
    void findPosts() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 10)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목" + i)
                                .content("본문" + i)
                                .build())
                .toList();
        postRepository.saveAll(requestPosts);

        //when then
        mockMvc.perform(get("/post?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].title").value("제목9"))
                .andExpect(jsonPath("$[0].content").value("본문9"))
                .andDo(print());
    }

    @Test
    @DisplayName("post 여러 개 조회 시 page를 0으로 요청하면, 1페이지를 조회한다")
    void findAllPosts() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 10)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목" + i)
                                .content("본문" + i)
                                .build())
                .toList();
        postRepository.saveAll(requestPosts);

        //when then
        mockMvc.perform(get("/post?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].title").value("제목9"))
                .andExpect(jsonPath("$[0].content").value("본문9"))
                .andDo(print());
    }

    @Test
    @DisplayName("post를 수정할 수 있다")
    void updatePost() throws Exception {
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
        mockMvc.perform(put("/post/{postId}", savedPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 id로 post를 수정할 수 있다")
    void updatePost_notExists() throws Exception {
        //given
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title("제목")
                .content("본문")
                .build();

        //when then
        mockMvc.perform(put("/post/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequest))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("post를 삭제할 수 있다")
    void deletePost() throws Exception {
        //given
        Post savedPost = postRepository.save(Post.builder()
                .title("제목")
                .content("본문")
                .build());

        //when then
        mockMvc.perform(delete("/post/{postId}", savedPost.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}
