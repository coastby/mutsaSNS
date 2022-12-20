package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;
    private String userName;

    public static UserJoinResponse from(User user){
        return new UserJoinResponse(user.getId(), user.getUserName());
    }
}
