package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeRestController.class)
class LikeRestControllerTest {
    @MockBean
    LikeService likeService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Nested
    @DisplayName("좋아요 누르기")
    @WithMockCustomUser
    class addLike{
        Integer postId = 1;
        @Test
        @DisplayName("좋아요 성공")
        void like_success() throws Exception {
            given(likeService.add(postId, "user")).willReturn("좋아요를 눌렀습니다.");

            mockMvc.perform(
                    post("/api/v1/posts/"+postId+"/likes")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."))
                    .andDo(print());
            verify(likeService).add(postId,"user");
        }
        @Test
        @DisplayName("좋아요 실패 - 로그인하지 않음")
        @WithAnonymousUser
        void like_fail_로그인안함() throws Exception {
            mockMvc.perform(
                            post("/api/v1/posts/"+postId+"/likes")
                                    .with(csrf()))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }
        @Test
        @DisplayName("좋아요 실패 - 해당 Post가 없는 경우")
        void like_fail_포스트없음() throws Exception {
            given(likeService.add(postId, "user")).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));
            mockMvc.perform(
                            post("/api/v1/posts/"+postId+"/likes")
                                    .with(csrf()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value("POST_NOT_FOUND"))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                    .andDo(print());
            verify(likeService).add(postId,"user");
        }
    }
    @Nested
    @DisplayName("좋아요 갯수 조회")
    @WithMockUser
    class likeCount{
        @Test
        @DisplayName("좋아요 갯수 조회 성공")
        void like_count_success() throws Exception {
            Integer postId = 1;
            given(likeService.getCount(postId)).willReturn(3);

            mockMvc.perform(get("/api/v1/posts/"+postId+"/likes")
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result").value("3"))
                    .andDo(print());
            verify(likeService).getCount(1);
        }
    }
}