package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.User;
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
