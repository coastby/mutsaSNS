package com.example.likelionmutsasnsproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    public ErrorCode errorCode;
    public String message;
}
