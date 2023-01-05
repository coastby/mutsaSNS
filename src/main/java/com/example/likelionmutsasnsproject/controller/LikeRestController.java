package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LikeRestController {
    private final LikeService likeService;
    @PostMapping(value = "/{postId}/likes")
    public Response<String> addLike(@PathVariable Integer postId, @ApiIgnore Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        String response = likeService.add(postId, userName);
        return Response.success(response);
    }
    @GetMapping(value = "/{postId}/likes")
    public Response<Integer> getCount(@PathVariable Integer postId){
        Integer count = likeService.getCount(postId);
        return Response.success(count);
    }

}
