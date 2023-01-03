package com.example.likelionmutsasnsproject.dto.post;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PostWorkRequest {
    private String title;
    private String body;
    public Post toEntity(User user){
        return Post.builder()
                .title(this.getTitle())
                .body(this.getBody())
                .user(user)
                .isDeleted(false)
                .build();
    }
    public Post editEntity(Post post) {
        return Post.builder()
                .id(post.getId())
                .title(this.getTitle())
                .body(this.getBody())
                .user(post.getUser())
                .isDeleted(false)
                .build();
    }
}
