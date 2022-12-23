package com.example.likelionmutsasnsproject.controller;

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
        return ResponseEntity.ok().body(Response.success(postResponses));
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<PostListResponse>> showPost(@PathVariable Integer id){
        PostListResponse response = postService.getById(id);
        return ResponseEntity.ok().body(Response.success(response));
    }

    @PostMapping
    public ResponseEntity<Response<PostWorkResponse>> add(@RequestBody PostWorkRequest request, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.add(request, userName);
        return ResponseEntity.created(URI.create("/api/v1/posts/"+response.getPostId()))
                .body(Response.success(response));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<PostWorkResponse>> delete(@PathVariable Integer id, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.delete(id, userName);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<PostWorkResponse>> edit
            (@RequestBody PostWorkRequest request, @PathVariable Integer id, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.update(id, request, userName);
        return ResponseEntity.ok().body(Response.success(response));
    }
}
