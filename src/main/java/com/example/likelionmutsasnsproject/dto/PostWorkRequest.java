package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

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
    public Post toEntity(Integer postId, User user){
        return Post.builder()
                .id(postId)
                .title(this.getTitle())
                .body(this.getBody())
                .user(user)
                .isDeleted(false)
                .build();
    }
}
