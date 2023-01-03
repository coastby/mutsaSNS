package com.example.likelionmutsasnsproject.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostWorkResponse {
    private String message;
    private Integer postId;
}
