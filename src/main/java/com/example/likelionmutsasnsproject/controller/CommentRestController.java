package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.dto.comment.CommentWorkResponse;
import com.example.likelionmutsasnsproject.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = "댓글")
public class CommentRestController {
    private final CommentService commentService;
    @Operation(summary = "포스트의 댓글 조회",
            description = "Path에 입력된 포스트의 댓글을 최신순으로 10개씩 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "페이지 번호", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지 당 댓글 수", defaultValue = "20")
    })
    @GetMapping(value = "/{postId}/comments")
    public Response<Page<CommentResponse>> showCommentList(
            @Parameter(description = "포스트ID") @PathVariable Integer postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<CommentResponse> comments = commentService.getAll(postId, pageable);
        return Response.success(comments);
    }
    @Operation(summary = "댓글 등록", description = "로그인 필요, 성공 시 201 반환")
    @ApiImplicitParam(name = "postId", value = "포스트 ID")
    @PostMapping(value = "/{postId}/comments")
    public ResponseEntity<Response<CommentResponse>> addComment(
            @RequestBody CommentRequest request, @Parameter(description = "포스트ID") @PathVariable Integer postId, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        CommentResponse response = commentService.add(request, postId, userName);
        return ResponseEntity
                .created(URI.create("/api/v1/posts/"+response.getPostId()+"/comments/"+response.getId()))
                .body(Response.success(response));
    }
    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글만 수정 가능")
    @PutMapping(value = "/{postId}/comments/{id}")
    public Response<CommentResponse> editComment(@Parameter(description = "포스트ID") @PathVariable Integer postId,
                                                 @Parameter(description = "댓글ID") @PathVariable Integer id,
                                                 Authentication authentication, @RequestBody CommentRequest request){
        String userName = authentication.getPrincipal().toString();
        CommentResponse response = commentService.edit(postId, id, request, userName);
        return Response.success(response);
    }
    @Operation(summary = "댓글 삭제", description = "본인이 작성한 댓글만 삭제 가능")
    @DeleteMapping(value = "/{postId}/comments/{id}")
    public Response<CommentWorkResponse> edit(@Parameter(description = "포스트ID") @PathVariable Integer postId,
                                              @Parameter(description = "댓글ID") @PathVariable Integer id,
                                              Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        CommentWorkResponse response = commentService.delete(postId, id, userName);
        return Response.success(response);
    }
}
