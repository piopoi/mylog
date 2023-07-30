package com.mysns.api.request;

import com.mysns.api.domain.Post;
import com.mysns.api.exception.InvalidRequestException;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class PostCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    private String content;

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    public void validate() {
        //TODO title 글자 수 제한
        //TODO content 글자 수 제한
        if (title.contains("욕설")) {
            throw new InvalidRequestException("title", "욕설을 포함할 수 없습니다.");
        }
    }
}
