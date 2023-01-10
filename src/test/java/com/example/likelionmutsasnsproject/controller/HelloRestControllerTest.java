package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.service.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HelloRestController.class)
@WithMockUser
class HelloRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HelloService helloService;

    @Test
    @DisplayName("good")
    @WithAnonymousUser
    void good() throws Exception {

        mockMvc.perform(
                get("/api/v1/nice"))
                .andExpect(status().isFound())
//                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andDo(print());
    }
    @Test
    @DisplayName("자리수 합 더하기")
    void sum() throws Exception {
        int num = 12345;
        given(helloService.sumOfDigit(num)).willReturn("15");

        MvcResult result = mockMvc.perform(
                                get("/api/v1/hello/"+num)
                                        .with(csrf()))
                                .andExpect(status().isOk())
                                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals("15", content);
    }

}