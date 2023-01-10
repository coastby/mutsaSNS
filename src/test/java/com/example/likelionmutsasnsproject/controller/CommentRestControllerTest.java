package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.dto.comment.CommentWorkResponse;
import com.example.likelionmutsasnsproject.exception.CommentException;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            .lastModifiedAt("2022/01/01 12:12:12")
            .build();
    CommentRequest request = new CommentRequest("안녕");

    /** 댓글 리스트 조회**/
    @Test
    @DisplayName("댓글 리스트 조회 성공")
    @WithMockUser
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
    @WithMockUser
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
                    .andExpect(status().isFound())
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
    @Nested
    @DisplayName("댓글 수정")
    @WithMockUser
    class editTest {
        @Test
        @DisplayName("댓글 수정 성공")
        void edit_success() throws Exception {
            Integer commentId = response.getId();
            given(commentService.edit(postId, commentId, request, "user")).willReturn(response);

            mockMvc.perform(
                            put("/api/v1/posts/" + postId + "/comments/"+commentId)
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.id").value(1))
                    .andExpect(jsonPath("$.result.comment").value("안녕"))
                    .andExpect(jsonPath("$.result.userName").value("user"))
                    .andExpect(jsonPath("$.result.postId").value(postId))
                    .andExpect(jsonPath("$.result.createdAt").exists())
                    .andExpect(jsonPath("$.result.lastModifiedAt").exists())
                    .andDo(print());
            verify(commentService).edit(postId, commentId, request, "user");
        }
        @Test
        @DisplayName("댓글 수정 실패 - 인증 실패")
        @WithAnonymousUser
        void edit_fail_인증실패() throws Exception {
            mockMvc.perform(
                            put("/api/v1/posts/" + postId + "/comments/"+1)
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isFound())
                    .andDo(print());
        }
        @Test
        @DisplayName("댓글 수정 실패 - Post 없는 경우")
        void edit_fail_post없음() throws Exception {
            Integer commentId = response.getId();
            given(commentService.edit(postId, commentId, request, "user"))
                    .willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(
                            put("/api/v1/posts/" + postId + "/comments/"+commentId)
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                    .andDo(print());
            verify(commentService).edit(postId, commentId, request, "user");
        }
        @Test
        @DisplayName("댓글 수정 실패 - 작성자 불일치")
        void edit_fail_작성자불일치() throws Exception {
            Integer commentId = response.getId();
            given(commentService.edit(postId, commentId, request, "user"))
                    .willThrow(new PostException(ErrorCode.INVALID_PERMISSION));

            mockMvc.perform(
                            put("/api/v1/posts/" + postId + "/comments/"+commentId)
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_PERMISSION.getMessage()))
                    .andDo(print());
            verify(commentService).edit(postId, commentId, request, "user");
        }
        @Test
        @DisplayName("댓글 수정 실패 - DB에러")
        void edit_fail_DB에러() throws Exception {
            Integer commentId = response.getId();
            given(commentService.edit(postId, commentId, request, "user"))
                    .willThrow(new PostException(ErrorCode.DATABASE_ERROR));

            mockMvc.perform(
                            put("/api/v1/posts/" + postId + "/comments/"+commentId)
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(request))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.DATABASE_ERROR.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.DATABASE_ERROR.getMessage()))
                    .andDo(print());
            verify(commentService).edit(postId, commentId, request, "user");
        }
    }
    @Nested
    @DisplayName("댓글 삭제")
    @WithMockUser
    class deleteTest {
        @Test
        @DisplayName("댓글 삭제 성공")
        void delete_success() throws Exception {
            Integer commentId = response.getId();
            CommentWorkResponse workResponse = CommentWorkResponse.builder()
                    .message("댓글 삭제 완료")
                    .id(commentId)
                    .build();
            given(commentService.delete(postId, commentId, "user")).willReturn(workResponse);

            mockMvc.perform(
                            delete("/api/v1/posts/" + postId + "/comments/" + commentId)
                                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.message").value("댓글 삭제 완료"))
                    .andExpect(jsonPath("$.result.id").value(commentId))
                    .andDo(print());
            verify(commentService).delete(postId, commentId, "user");
        }
        @Test
        @DisplayName("댓글 삭제 실패 - 인증 실패")
        @WithAnonymousUser
        void delete_fail_인증실패() throws Exception {
            mockMvc.perform(
                            delete("/api/v1/posts/" + postId + "/comments/"+1)
                                    .with(csrf()))
                    .andExpect(status().isFound())
                    .andDo(print());
        }
        @Test
        @DisplayName("댓글 삭제 실패 - Post 없는 경우")
        void delete_fail_post없음() throws Exception {
            given(commentService.delete(postId, 1, "user"))
                    .willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

            mockMvc.perform(
                            delete("/api/v1/posts/" + postId + "/comments/" + 1)
                                    .with(csrf()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.POST_NOT_FOUND.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                    .andDo(print());
            verify(commentService).delete(postId, 1, "user");
        }
        @Test
        @DisplayName("댓글 삭제 실패 - 작성자 불일치")
        void delete_fail_작성자불일치() throws Exception {
            given(commentService.delete(postId, 1, "user"))
                    .willThrow(new UserException(ErrorCode.INVALID_PERMISSION));

            mockMvc.perform(
                            delete("/api/v1/posts/" + postId + "/comments/" + 1)
                                    .with(csrf()))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_PERMISSION.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_PERMISSION.getMessage()))
                    .andDo(print());
            verify(commentService).delete(postId, 1, "user");
        }
        @Test
        @DisplayName("댓글 삭제 실패 - DB에러")
        void delete_fail_DB에러() throws Exception {
            given(commentService.delete(postId, 1, "user"))
                    .willThrow(new PostException(ErrorCode.INTERNAL_SERVER_ERROR));

            mockMvc.perform(
                            delete("/api/v1/posts/" + postId + "/comments/" + 1)
                                    .with(csrf()))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.resultCode").value("ERROR"))
                    .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INTERNAL_SERVER_ERROR.name()))
                    .andExpect(jsonPath("$.result.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()))
                    .andDo(print());
            verify(commentService).delete(postId, 1, "user");
        }
    }
}