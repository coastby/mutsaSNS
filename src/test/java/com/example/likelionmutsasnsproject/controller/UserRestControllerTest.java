package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.UserJoinRequest;
import com.example.likelionmutsasnsproject.dto.UserJoinResponse;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@WithMockUser
class UserRestControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    UserJoinRequest userJoinRequest = new UserJoinRequest("hoon", "hi");



    /**
     * 회원가입 테스트
     **/
    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {
        given(userService.join(userJoinRequest)).willReturn(new UserJoinResponse(0, "hoon"));

        mockMvc.perform(
                post("/api/v1/users/join")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(userJoinRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.userName").value("hoon"))
                .andExpect(jsonPath("$.result.userId").value(0))
                .andDo(print());
        verify(userService).join(userJoinRequest);
    }
    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void join_fail_duplicated_id() throws Exception {
        given(userService.join(userJoinRequest))
                .willThrow(new UserException(UserErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(
                        post("/api/v1/users/join")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(userJoinRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result.errorCode").value("DUPLICATED_USER_NAME"))
                .andExpect(jsonPath("$.result.message").value("UserName이 중복됩니다."))
                .andDo(print());
        verify(userService).join(userJoinRequest);
    }
    /**
     * 로그인 테스트
     **/
    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        given(userService.join(userJoinRequest))
                .willThrow(new UserException(UserErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(
                        post("/api/v1/users/join")
                                .with(csrf())
                                .content(objectMapper.writeValueAsBytes(userJoinRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result.errorCode").value("DUPLICATED_USER_NAME"))
                .andExpect(jsonPath("$.result.message").value("UserName이 중복됩니다."))
                .andDo(print());
        verify(userService).join(userJoinRequest);

    }

}