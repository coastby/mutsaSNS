package com.example.likelionmutsasnsproject.dto.comment;

import com.example.likelionmutsasnsproject.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Builder
public class CommentResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private String createdAt;
    private String lastModifiedAt;
    public static CommentResponse from (Comment comment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUsername())
                .postId(comment.getPost().getId())
                .createdAt(dateFormat.format(comment.getCreatedAt()))
                .lastModifiedAt(dateFormat.format(comment.getUpdatedAt()))
                .build();
    }
}
