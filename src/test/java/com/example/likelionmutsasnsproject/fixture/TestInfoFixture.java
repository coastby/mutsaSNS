package com.example.likelionmutsasnsproject.fixture;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class TestInfoFixture {
    public static TestInfo get(){
        return TestInfo.builder()
                .postId(1)
                .userId(1)
                .userName("hoon")
                .password("password")
                .title("title")
                .body("body")
                .build();
    }

    @Getter
    @Builder
    @Setter
    public static class TestInfo{
        private Integer postId;
        private Integer userId;
        private String userName;
        private String password;
        private String title;
        private String body;
    }

}