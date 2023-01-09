package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = "좋아요")
public class LikeRestController {
    private final LikeService likeService;
    @Operation(summary = "좋아요 누르기", description = "게시글에 좋아요하는 기능. 한 번만 가능.")
    @PostMapping(value = "/{postId}/likes")
    public Response<String> addLike(@Parameter(description = "포스트ID") @PathVariable Integer postId, @AuthenticationPrincipal UserDetails user){
        String response = likeService.add(postId, user.getUsername());
        return Response.success(response);
    }
    @Operation(summary = "내가 좋아요한 게시글 아이디 조회", description = "포스트의 좋아요 갯수 조회")
    @GetMapping(value = "/likes/my")
    public Response<List> getMyLikeList(@AuthenticationPrincipal UserDetails user){
        List<Integer> likedPosts = likeService.getMyLikeList(user.getUsername());
        return Response.success(likedPosts);
    }


    @Operation(summary = "좋아요 조회", description = "포스트의 좋아요 갯수 조회")
    @GetMapping(value = "/{postId}/likes")
    public Response<Integer> getCount(@Parameter(description = "포스트ID") @PathVariable Integer postId){
        Integer count = likeService.getCount(postId);
        return Response.success(count);
    }
}
