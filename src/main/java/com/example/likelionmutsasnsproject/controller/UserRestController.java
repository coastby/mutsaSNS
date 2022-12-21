package com.example.likelionmutsasnsproject.controller;


import com.example.likelionmutsasnsproject.dto.*;
import com.example.likelionmutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;

    @PostMapping(value = "/join")       //회원가입
    public ResponseEntity<Response<UserJoinResponse>> join(@RequestBody UserJoinRequest request){
        UserJoinResponse userJoinResponse = userService.join(request);
        return ResponseEntity.created(URI.create("/api/v1/users/"+userJoinResponse.getUserId()))     //성공 시 상태코드 : 201
                .body(Response.success(userJoinResponse));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok().body(Response.success(response));
    }

}
