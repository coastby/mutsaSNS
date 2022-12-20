package com.example.likelionmutsasnsproject.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    public static Response<Void> error(String resultCode){
        return new Response(resultCode, null);
    }
    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }
}
