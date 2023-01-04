package com.example.likelionmutsasnsproject.fixture;

import com.example.likelionmutsasnsproject.domain.BaseEntity;
import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;

public class CommentEntityFixture {
    public static Comment get (String userName, String password, boolean deleted){
        Comment comment = Comment.builder()
                .id(1)
                .comment("안녕")
                .user(UserEntityFixture.get(userName, password))
                .post(PostEntityFixture.get(userName, password, false))
                .build();

        ReflectionTestUtils.setField(
                comment,
                BaseEntity.class,
                "createdAt",
                new Timestamp(System.currentTimeMillis()),
                Timestamp.class
        );
        ReflectionTestUtils.setField(
                comment,
                BaseEntity.class,
                "updatedAt",
                new Timestamp(System.currentTimeMillis()),
                Timestamp.class
        );
        if(deleted) {
            ReflectionTestUtils.setField(
                    comment,
                    BaseEntity.class,
                    "deletedAt",
                    new Timestamp(System.currentTimeMillis()),
                    Timestamp.class
            );
        }
        return comment;
    }
}
