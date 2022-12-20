package com.example.likelionmutsasnsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static Response<String> error(String resultCode, String message){
        return new Response(resultCode, message);
    }
    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }
}
