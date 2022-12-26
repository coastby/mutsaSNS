package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        given(postService.add(any(), any())).willReturn(new PostWorkResponse("포스트 등록 완료", 0));

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
    @DisplayName("포스트 작성 성공 - @WithMockUser 사용")
    void post_add_success2() throws Exception {
        given(postService.add(any(), any())).willReturn(new PostWorkResponse("포스트 등록 완료", 0));

        mockMvc.perform(
                        post("/api/v1/posts")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.message").value("포스트 등록 완료"))
                .andExpect(jsonPath("$.result.postId").value(0))
                .andDo(print());
    }
    @Test
    @DisplayName("포스트 작성 실패 - 유효하지 않은 토큰")
    @WithAnonymousUser
    void post_add_fail() throws Exception {
        given(postService.add(any(), any())).willThrow(new UserException(ErrorCode.INVALID_PERMISSION));


        mockMvc.perform(
                        post("/api/v1/posts")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
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
    /**
     * 포스트 상세조회
     * **/
    @Test
    @DisplayName("포스트 상세 조회 성공")
    void show_post_success() throws Exception {
        Integer postId = 1;
        //가짜 결과값
        PostListResponse response = PostListResponse.builder()
                .id(postId)
                .title("제목")
                .body("내용")
                .userName("작성자")
                .createdAt("2022/12/25 16:28:42")
                .lastModifiedAt("2022/12/25 16:28:42")
                .build();

        given(postService.getById(postId)).willReturn(response);

        mockMvc.perform(get("/api/v1/posts/"+postId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(postId))
                .andExpect(jsonPath("$['result']['title']").exists())
                .andExpect(jsonPath("$['result']['body']").exists())
                .andExpect(jsonPath("$['result']['userName']").exists())
                .andDo(print());
    }
    /**
     * 포스트 수정
     * **/
    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockCustomUser
    void post_edit_success() throws Exception {
        Integer postId = 0;
        given(postService.update(postId, postWorkRequest,"user")).willReturn(new PostWorkResponse("포스트 수정 완료", postId));

        mockMvc.perform(
                        put("/api/v1/posts/"+postId)
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("포스트 수정 완료"))
                .andExpect(jsonPath("$.result.postId").value(0))
                .andDo(print());
        verify(postService).update(postId, postWorkRequest,"user");
    }
    @Test
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    @WithMockCustomUser
    void post_edit_fail_작성자불일치() throws Exception {
        Integer postId = 0;
        given(postService.update(postId, postWorkRequest,"user")).willThrow(new UserException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(
                        put("/api/v1/posts/"+postId)
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("사용자가 권한이 없습니다."))
                .andDo(print());
        verify(postService).update(postId, postWorkRequest,"user");
    }
    @Test
    @DisplayName("포스트 수정 실패 - DB에러")
    @WithMockCustomUser
    void post_edit_fail_DB에러() throws Exception {
        Integer postId = 0;
        given(postService.update(postId, postWorkRequest,"user")).willThrow(new PostException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(
                        put("/api/v1/posts/"+postId)
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(postWorkRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("DATABASE_ERROR"))
                .andExpect(jsonPath("$.result.message").value("DB에러"))
                .andDo(print());
        verify(postService).update(postId, postWorkRequest,"user");
    }

}