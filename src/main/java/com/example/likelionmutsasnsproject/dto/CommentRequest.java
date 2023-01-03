package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentRequest {
    private String comment;
    public Comment toEntity(Post post, User user){
        return Comment.builder()
                .post(post)
                .user(user)
                .comment(this.comment)
                .build();
    }
}
