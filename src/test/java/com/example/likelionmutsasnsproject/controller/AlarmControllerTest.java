package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import com.example.likelionmutsasnsproject.dto.alarrm.AlarmResponse;
import com.example.likelionmutsasnsproject.dto.alarrm.AlarmType;
import com.example.likelionmutsasnsproject.service.AlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AlarmController.class)
//@WithMockCustomUser
@WithMockUser
class AlarmControllerTest {
    @MockBean
    AlarmService alarmService;
    @Autowired
    private MockMvc mockMvc;

    Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt");
    Integer postId = 1;
    AlarmResponse response = AlarmResponse.builder()
            .id(1L)
            .alarmType(AlarmType.NEW_COMMENT_ON_POST)
            .targetId(postId)
            .fromUserId(1)
            .text(AlarmType.NEW_COMMENT_ON_POST.getText())
            .build();
    @Test
    @DisplayName("알람 리스트 조회")
    void alarm_list_success() throws Exception {
        Page<AlarmResponse> responsePage = new PageImpl<>(List.of(response));
        given(alarmService.getAlarms("user", pageable)).willReturn(responsePage);

        mockMvc.perform(
                get("/api/v1/alarms")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").exists())
                .andExpect(jsonPath("$['result']['content'][0]['id']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['alarmType']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['targetId']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['text']").exists())
                .andDo(print());
        verify(alarmService).getAlarms("user", pageable);
    }
    @Test
    @DisplayName("알람 조회 실패 - 로그인 안함")
    @WithAnonymousUser
    void alarm_list_fail_인증안함() throws Exception {
        mockMvc.perform(
                        get("/api/v1/alarms")
                                .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}