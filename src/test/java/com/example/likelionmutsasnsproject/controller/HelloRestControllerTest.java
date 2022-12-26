package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(HelloRestController.class)
class HelloRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("good")
    @WithAnonymousUser
    void good() throws Exception {

        mockMvc.perform(
                get("/api/v1/nice"))
                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andDo(print());
    }

}