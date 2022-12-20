package com.example.likelionmutsasnsproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostException extends RuntimeException{
    private PostErrorCode postErrorCode;
    private String message;

    @Override
    public String toString() {
        if (message == null) return postErrorCode.getMessage();
        return String.format("%s. %s", postErrorCode.getMessage(), message);
    }
}
