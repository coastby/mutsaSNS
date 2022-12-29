package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@Builder
public class PostResponse {
    private Integer id;
    private String title;
    private String body;
    private String userName;
    private String createdAt;
    private String lastModifiedAt;

    public static PostResponse from(Post post){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(dateFormat.format(post.getCreatedAt()))
                .lastModifiedAt(dateFormat.format(post.getUpdatedAt()))
                .build();
    }
}
