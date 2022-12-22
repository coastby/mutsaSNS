package com.example.likelionmutsasnsproject.controller;


import com.example.likelionmutsasnsproject.dto.*;
import com.example.likelionmutsasnsproject.exception.ErrorResponse;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.UserLoginService;
import com.example.likelionmutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;
    private final UserLoginService userLoginService;

    @PostMapping(value = "/join")       //회원가입
    public ResponseEntity<Response<UserJoinResponse>> join(@RequestBody UserJoinRequest request){
        UserJoinResponse userJoinResponse = userService.join(request);
        return ResponseEntity.created(URI.create("/api/v1/users/"+userJoinResponse.getUserId()))     //성공 시 상태코드 : 201
                .body(Response.success(userJoinResponse));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userLoginService.login(request);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @GetMapping(value = "/exception")
    public ResponseEntity<?> userException(){
        UserException e = new UserException(UserErrorCode.INVALID_PERMISSION);
        return ResponseEntity.status(e.getUserErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getUserErrorCode().name(), e.toString())));
    }

}
