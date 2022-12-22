package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.PostAddRequest;
import com.example.likelionmutsasnsproject.dto.PostWorkResponse;
import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {
    private final PostService postService;
    @GetMapping
    public String test(){
        return "hello";
    }
    @PostMapping
    public ResponseEntity<Response<PostWorkResponse>> add(@RequestBody PostAddRequest request, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        log.error("userName: {}", userName);
        PostWorkResponse response = postService.add(request, userName);
        return ResponseEntity.created(URI.create("/api/v1/posts/"+response.getPostId()))
                .body(Response.success(response));
    }
}
