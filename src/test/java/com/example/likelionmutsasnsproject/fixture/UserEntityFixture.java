package com.example.likelionmutsasnsproject.fixture;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserRole;

public class UserEntityFixture {
    public static User get(String userName, String password){
        return User.builder()
                .id(1)
                .userName(userName)
                .password(password)
                .role(UserRole.USER)
                .build();

    }
}
