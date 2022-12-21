package com.example.likelionmutsasnsproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException{
    private UserErrorCode userErrorCode;
    private String message;

    public UserException(UserErrorCode userErrorCode) {
        this.userErrorCode = userErrorCode;
    }

    @Override
    public String toString() {
        if (message == null) return userErrorCode.getMessage();
        return String.format("%s %s", userErrorCode.getMessage(), message);
    }
}
