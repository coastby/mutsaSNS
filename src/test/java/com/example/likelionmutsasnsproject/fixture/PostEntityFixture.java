package com.example.likelionmutsasnsproject.fixture;

import com.example.likelionmutsasnsproject.domain.Post;

public class PostEntityFixture {
    public static Post get (String userName, String password){
        return Post.builder()
                .id(1)
                .user(UserEntityFixture.get(userName, password))
                .title("title")
                .body("body")
                .build();
    }
}
