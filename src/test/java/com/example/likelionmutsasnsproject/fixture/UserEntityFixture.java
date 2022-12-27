package com.example.likelionmutsasnsproject.fixture;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserRole;

public class UserEntityFixture {
    public static User get(String userName, String password){
        return User.builder()
                .id(1)
                .userName(userName)
                .password(password)
                .role(UserRole.ROLE_USER)
                .build();

    }
    public static User getADMIN(String userName, String password){
        return User.builder()
                .id(1)
                .userName(userName)
                .password(password)
                .role(UserRole.ROLE_ADMIN)
                .build();

    }
}
