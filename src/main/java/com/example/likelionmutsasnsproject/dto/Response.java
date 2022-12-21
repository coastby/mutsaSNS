package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.exception.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static Response<ErrorResult> error(ErrorResult errorResult){
        return new Response<>("ERROR", errorResult);
    }
    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }
}
