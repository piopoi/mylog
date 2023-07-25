package com.mysns.api.repository;

import static com.mysns.api.domain.QPost.post;

import com.mysns.api.domain.Post;
import com.mysns.api.request.PostSearchRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearchRequest postSearchRequest) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearchRequest.getSize())
                .offset(postSearchRequest.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
