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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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