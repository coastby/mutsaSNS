package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.CommentRequest;
import com.example.likelionmutsasnsproject.dto.CommentResponse;
import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.service.CommentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;
    @Operation(summary = "전체 댓글 리스트 조회",
            description = "포스트 최신순으로 10개씩 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "페이지 번호", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지 당 댓글 수", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/{postId}/comments")
    public ResponseEntity<Response<Page>> showCommentList(
            @PathVariable Integer postId,
            @ApiIgnore @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<CommentResponse> comments = commentService.getAll(postId, pageable);
        return ResponseEntity.ok().body(Response.success(comments));
    }
    @PostMapping(value = "/{postId}/comments")
    public ResponseEntity<Response<CommentResponse>> addComment(
            @RequestBody CommentRequest request, @PathVariable Integer postId, @ApiIgnore Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        CommentResponse response = commentService.add(request, postId, userName);
        return ResponseEntity
                .created(URI.create("/api/v1/posts/"+response.getPostId()+"/comments/"+response.getId()))
                .body(Response.success(response));
    }

}
