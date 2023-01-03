package com.example.likelionmutsasnsproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserLoginRequest {
    private String userName;
    private String password;
}
