package com.example.likelionmutsasnsproject.fixture;

import com.example.likelionmutsasnsproject.domain.BaseEntity;
import com.example.likelionmutsasnsproject.domain.Post;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;

public class PostEntityFixture {
    public static Post get (String userName, String password, boolean deleted){
        Post post = Post.builder()
                .id(1)
                .user(UserEntityFixture.get(userName, password))
                .title("title")
                .body("body")
                .isDeleted(deleted)
                .build();
        ReflectionTestUtils.setField(
                post,
                BaseEntity.class,
                "createdAt",
                new Timestamp(System.currentTimeMillis()),
                Timestamp.class
        );
        ReflectionTestUtils.setField(
                post,
                BaseEntity.class,
                "updatedAt",
                new Timestamp(System.currentTimeMillis()),
                Timestamp.class
        );
        return post;
    }
}
