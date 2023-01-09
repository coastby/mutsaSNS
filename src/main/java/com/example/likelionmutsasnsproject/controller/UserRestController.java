package com.example.likelionmutsasnsproject.controller;


import com.example.likelionmutsasnsproject.dto.*;
import com.example.likelionmutsasnsproject.dto.user.*;
import com.example.likelionmutsasnsproject.exception.ErrorResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.UserLoginService;
import com.example.likelionmutsasnsproject.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "사용자")
public class UserRestController {
    private final UserService userService;
    private final UserLoginService userLoginService;

    @Operation(summary = "회원 가입", description = "아이디 중복 불가능")
    @PostMapping(value = "/join")       //회원가입
    public ResponseEntity<Response<UserJoinResponse>> join(@RequestBody UserJoinRequest request){
        UserJoinResponse userJoinResponse = userService.join(request);
        return ResponseEntity.created(URI.create("/api/v1/users/"+userJoinResponse.getUserId()))     //성공 시 상태코드 : 201
                .body(Response.success(userJoinResponse));
    }

    @Operation(summary = "로그인", description = "로그인 시 jwt 발급")
    @PostMapping(value = "/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userLoginService.login(request);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @ApiIgnore
    @GetMapping(value = "/exception")
    public ResponseEntity<?> userException(){
        UserException e = new UserException(ErrorCode.INVALID_PERMISSION);
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorResponse(e.getErrorCode(), e.toString())));
    }
    @Operation(summary = "권한변경", description = "ADMIN <-> USER")
    @PostMapping(value = "/{id}/role/change")
    public ResponseEntity<Response<UserRoleResponse>> changeRole
            (@ApiParam(value = "userId", required = true, example = "1") @PathVariable Integer id,
             @RequestBody UserRoleRequest request, @ApiIgnore Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        UserRoleResponse response = userService.changeRole(id, request.getRole(), userName);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @GetMapping(value = "/my")
    public Response<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails user){
        UserResponse response = userService.getMyInfo(user.getUsername());
        return Response.success(response);
    }
}
