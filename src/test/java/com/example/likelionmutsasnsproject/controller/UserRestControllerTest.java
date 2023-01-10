package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import com.example.likelionmutsasnsproject.dto.user.*;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.service.UserLoginService;
import com.example.likelionmutsasnsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    @MockBean
    UserLoginService userLoginService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    UserJoinRequest userJoinRequest = new UserJoinRequest("hoon", "hi");
    UserLoginRequest userLoginRequest = new UserLoginRequest("hoon", "hi");


    @Nested
    @DisplayName("회원가입")
    class join{
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
                    .willThrow(new UserException(ErrorCode.DUPLICATED_USER_NAME));

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
    @Nested
    @DisplayName("로그인")
    class login {
        @Test
        @DisplayName("로그인 성공")
        void login_success() throws Exception {
            given(userLoginService.login(userLoginRequest))
                    .willReturn(new UserLoginResponse("token"));

            mockMvc.perform(
                            post("/api/v1/users/login")
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(userLoginRequest))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.jwt").value("token"))
                    .andDo(print());
            verify(userLoginService).login(userLoginRequest);
        }
        @Test
        @DisplayName("로그인 실패 - 아이디가 없는 경우")
        void login_fail_유저네임_없음() throws Exception {
            given(userLoginService.login(userLoginRequest))
                    .willThrow(new UserException(ErrorCode.USERNAME_NOT_FOUND));

            mockMvc.perform(
                            post("/api/v1/users/login")
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(userLoginRequest))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.result.errorCode").value("USERNAME_NOT_FOUND"))
                    .andExpect(jsonPath("$.result.message").value("Not founded"))
                    .andDo(print());
            verify(userLoginService).login(userLoginRequest);
        }
        @Test
        @DisplayName("로그인 실패 - 비밀번호 틀림")
        void login_fail_비밀번호_틀림() throws Exception {
            given(userLoginService.login(userLoginRequest))
                    .willThrow(new UserException(ErrorCode.INVALID_PASSWORD));

            mockMvc.perform(
                            post("/api/v1/users/login")
                                    .with(csrf())
                                    .content(objectMapper.writeValueAsBytes(userLoginRequest))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.result.errorCode").value("INVALID_PASSWORD"))
                    .andExpect(jsonPath("$.result.message").value("패스워드가 잘못되었습니다."))
                    .andDo(print());
            verify(userLoginService).login(userLoginRequest);
        }
    }
    @Nested
    @DisplayName("권한 변경")
    class role{
        @Test
        @DisplayName("권한 변경 성공")
        @WithMockCustomUser(roles = "ROLE_ADMIN")
        void change_role_success() throws Exception {
            Integer userId = 1;
            String role = "ADMIN";
            UserRoleRequest request = new UserRoleRequest(role);
            UserRoleResponse response = new UserRoleResponse(role+"(으)로 변경되었습니다.", "user");

            given(userService.changeRole(userId, role, "user")).willReturn(response);

            mockMvc.perform(
                    post("/api/v1/users/"+userId+"/role/change")
                            .with(csrf())
                            .content(objectMapper.writeValueAsBytes(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.message").value(role+"(으)로 변경되었습니다."))
                    .andExpect(jsonPath("$.result.userName").value("user"))
                    .andDo(print());
        }
    }
}