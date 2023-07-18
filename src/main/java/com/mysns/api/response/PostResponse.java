package com.mysns.api.response;

import com.mysns.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
