package com.example.likelionmutsasnsproject.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentWorkResponse {
    private String message;
    private Integer id;
}
