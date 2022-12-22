package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.dto.*;
import com.example.likelionmutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {
    private final PostService postService;
    @GetMapping
    public ResponseEntity<Response<Page>> showListPage(
            @PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostListResponse> postResponses = postService.getAll(pageable);
//        PostList response = new PostList(posts, pageable);
        log.info(pageable.toString());
        return ResponseEntity.ok().body(Response.success(postResponses));
    }
    @PostMapping
    public ResponseEntity<Response<PostWorkResponse>> add(@RequestBody PostAddRequest request, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        log.info("userName: {}", userName);
        PostWorkResponse response = postService.add(request, userName);
        return ResponseEntity.created(URI.create("/api/v1/posts/"+response.getPostId()))
                .body(Response.success(response));
    }
}
