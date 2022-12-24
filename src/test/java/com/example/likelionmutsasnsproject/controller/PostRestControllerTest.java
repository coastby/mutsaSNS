package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.PostWorkRequest;
import com.example.likelionmutsasnsproject.dto.PostListResponse;
import com.example.likelionmutsasnsproject.dto.PostWorkResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;


@WebMvcTest(PostRestController.class)
@WithMockUser
class PostRestControllerTest {
    @MockBean
    PostService postService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    PostWorkRequest postWorkRequest = new PostWorkRequest("merry", "christmas");
    Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt");


    /**
     * 포스트 작성 테스트
     * **/
    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockCustomUser
    void post_add_success() throws Exception {
        given(postService.add(postWorkRequest, "user")).willReturn(new PostWorkResponse("포스트 등록 완료", 0));

        mockMvc.perform(
                post("/api/v1/posts")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(postWorkRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.message").value("포스트 등록 완료"))
                .andExpect(jsonPath("$.result.postId").value(0))
                .andDo(print());
        verify(postService).add(postWorkRequest, "user");
    }
    @Test
    @DisplayName("포스트 작성 실패 - Bearer Token으로 보내지 않은 경우")
    @WithMockCustomUser
    void post_add_fail_토큰형식이상() throws Exception {
        given(postService.add(postWorkRequest, "user")).willThrow(new UserException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(
                        post("/api/v1/posts")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("사용자가 권한이 없습니다."))
                .andDo(print());
        verify(postService).add(postWorkRequest, "user");
    }
    @Test
    @DisplayName("포스트 작성 실패 - Jwt토큰 이상")
    @WithMockCustomUser
    void post_add_fail_토큰유효하지않음() throws Exception {
        given(postService.add(postWorkRequest, "user")).willThrow(new UserException(ErrorCode.INVALID_TOKEN));

        mockMvc.perform(
                        post("/api/v1/posts")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.result.message").value("잘못된 토큰입니다."))
                .andDo(print());
        verify(postService).add(postWorkRequest, "user");
    }

    /**
     * 포스트 조회 테스트
     * **/
    @Test
    @DisplayName("포스트 리스트 조회 성공")
    void show_post_list_success() throws Exception {
        //가짜 결과값
        List<PostListResponse> postList = List.of(PostListResponse.builder()
                        .title("제목")
                        .createdAt("날짜")
                        .build());
        Page<PostListResponse> response = new PageImpl<>(postList);

        given(postService.getAll(pageable)).willReturn(response);

        mockMvc.perform(get("/api/v1/posts")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").exists())
                .andExpect(jsonPath("$['result']['content'][0]['title']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['createdAt']").exists())
                .andDo(print());
    }

}