package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.CommentResponse;
import com.example.likelionmutsasnsproject.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


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

    /**
     * 댓글 리스트 조회
     * **/
    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void comment_list_success() throws Exception {
        Integer postId = 1;
        //가짜 Page 객체
        Page<CommentResponse> response = new PageImpl<>(List.of(CommentResponse.builder()
                .id(1)
                .comment("댓글")
                .postId(postId)
                .userName("user")
                .createdAt("2022/01/01 12:12:12")
                .build()));
        given(commentService.getAll(postId, pageable)).willReturn(response);

        mockMvc.perform(get("/api/v1/posts/"+postId+"/comments")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").exists())
                .andExpect(jsonPath("$['result']['content'][0]['comment']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['postId']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['userName']").exists())
                .andDo(print());
    }
}