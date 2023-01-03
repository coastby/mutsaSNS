package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.verify;



@WebMvcTest(CommentRestController.class)
@WithMockUser
class CommentRestControllerTest {
    @MockBean
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
    Integer postId = 1;
    CommentResponse response = CommentResponse.builder()
            .id(1)
            .comment("안녕")
            .postId(postId)
            .userName("user")
            .createdAt("2022/01/01 12:12:12")
            .build();
    CommentRequest request = new CommentRequest("안녕");

    /** 댓글 리스트 조회**/
    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void comment_list_success() throws Exception {
        //가짜 Page 객체
        Page<CommentResponse> responsePage = new PageImpl<>(List.of(response));
        given(commentService.getAll(postId, pageable)).willReturn(responsePage);

        mockMvc.perform(get("/api/v1/posts/"+postId+"/comments")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").exists())
                .andExpect(jsonPath("$['result']['content'][0]['comment']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['postId']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['userName']").exists())
                .andDo(print());
    }

    @Nested
    @DisplayName("댓글 작성")
    @WithMockCustomUser
    class addTest{
        @Test
        @DisplayName("댓글 작성 성공")
        void add_success() throws Exception {
            given(commentService.add(request, postId, "user")).willReturn(response);

            mockMvc.perform(
                    post("/api/v1/posts/"+postId+"/comments")
                            .with(csrf())
                            .content(objectMapper.writeValueAsBytes(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.result.id").value(1))
                    .andExpect(jsonPath("$.result.comment").value("안녕"))
                    .andExpect(jsonPath("$.result.userName").value("user"))
                    .andExpect(jsonPath("$.result.postId").value(postId))
                    .andExpect(jsonPath("$.result.createdAt").exists())
                    .andDo(print());
            verify(commentService).add(request, postId, "user");
        }
        @Test
        @DisplayName("댓글 작성 실패 - 로그인하지 않음")
        @WithAnonymousUser
        void add_fail_로그인안함() throws Exception {
            mockMvc.perform(
                    post("/api/v1/posts/"+postId+"/comments")
                            .with(csrf())
                            .content(objectMapper.writeValueAsBytes(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }
        @Test
        @DisplayName("댓글 작성 실패 - 게시물이 존재하지 않음")
        void add_fail_게시글없음() throws Exception {
            given(commentService.add(request, postId, "user")).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(
                            post("/api/v1/posts/"+postId+"/comments")
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value("POST_NOT_FOUND"))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                    .andDo(print());
            verify(commentService).add(request, postId, "user");
        }
    }
}