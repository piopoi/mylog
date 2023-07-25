package com.mysns.api.repository;

import com.mysns.api.domain.Post;
import com.mysns.api.request.PostSearchRequest;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearchRequest postSearchRequest);
}
