package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.*;
import com.example.likelionmutsasnsproject.dto.post.PostResponse;
import com.example.likelionmutsasnsproject.dto.post.PostWorkRequest;
import com.example.likelionmutsasnsproject.dto.post.PostWorkResponse;
import com.example.likelionmutsasnsproject.service.PostService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "포스트")
public class PostRestController {
    private final PostService postService;
    @Operation(summary = "전체 포스트 리스트 조회",
                description = "포스트 최신순으로 20개씩 조회, 삭제된 포스트는 조회되지 않는다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "페이지 번호", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지 당 포스트 수", defaultValue = "20")
    })
    @GetMapping
    public ResponseEntity<Response<Page>> showAllListPage(
            @ApiIgnore @PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostResponse> postResponses = postService.getAll(pageable);
        return ResponseEntity.ok().body(Response.success(postResponses));
    }
    @Operation(summary = "단일 포스트 조회")
    @ApiImplicitParam(name = "id", value = "포스트 ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<PostResponse>> showPost(@Parameter(description = "포스트ID") @PathVariable Integer id){
        PostResponse response = postService.getById(id);
        return ResponseEntity.ok().body(Response.success(response));
    }

    @Operation(summary = "포스트 작성", description = "로그인 후 작성 가능")
    @PostMapping
    public ResponseEntity<Response<PostWorkResponse>> add(@RequestBody PostWorkRequest request, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.add(request, userName);
        return ResponseEntity.created(URI.create("/api/v1/posts/"+response.getPostId()))
                .body(Response.success(response));
    }
    @Operation(summary = "포스트 삭제", description = "로그인 한 사용자와 작성자가 동일해야 삭제 가능")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<PostWorkResponse>> delete(@Parameter(description = "포스트ID") @PathVariable Integer id, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.delete(id, userName);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @Operation(summary = "포스트 수정", description = "로그인 한 사용자와 작성자가 동일해야 수정 가능")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<PostWorkResponse>> edit
            (@RequestBody PostWorkRequest request, @Parameter(description = "포스트ID") @PathVariable Integer id, Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        PostWorkResponse response = postService.update(id, request, userName);
        return ResponseEntity.ok().body(Response.success(response));
    }
    @Operation(summary = "마이피드",
            description = "내가 작성한 글만 보이는 기능")
    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "페이지 번호", defaultValue = "0")
    @GetMapping(value = "/my")
    public Response<Page> showMyListPage(
            @PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication){
        String userName = authentication.getPrincipal().toString();
        Page<PostResponse> postResponses = postService.getMyPosts(userName, pageable);
        return Response.success(postResponses);
    }
}
