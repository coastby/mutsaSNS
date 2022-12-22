package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Builder
public class PostListResponse {
    private Integer id;
    private String title;
    private String body;
    private String userName;
    private String createdAt;
    private String lastModifiedAt;

    public static PostListResponse from(Post post){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(dateFormat.format(post.getCreatedAt()))
                .lastModifiedAt(dateFormat.format(post.getUpdatedAt()))
                .build();
    }

}
