package com.mysns.api.request;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearchRequest {

    private static final int MIN_PAGE = 1;
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return (long) (max(page, MIN_PAGE) - 1) * min(size, MAX_SIZE);
    }
}
