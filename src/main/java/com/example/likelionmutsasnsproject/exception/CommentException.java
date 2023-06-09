package com.example.likelionmutsasnsproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    public CommentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        if (message == null) return errorCode.getMessage();
        return String.format("%s %s", errorCode.getMessage(), message);
    }
}
