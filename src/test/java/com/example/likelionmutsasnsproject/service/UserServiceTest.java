package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.UserRoleResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.fixture.TestInfoFixture;
import com.example.likelionmutsasnsproject.fixture.UserEntityFixture;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository = mock(UserRepository.class);
    private TestInfoFixture.TestInfo fixture;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository, mock(BCryptPasswordEncoder.class));
        fixture = TestInfoFixture.get();
    }
    /**
     * 권한 설정
     * **/
    @Test
    @DisplayName("권한 변경 성공 : USER -> ADMIN")
    void change_role_success(){
        User user = UserEntityFixture.get("user", "password");
        User auth = UserEntityFixture.getADMIN("auth", "pw");
        //given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByUserName(auth.getUsername())).willReturn(Optional.of(auth));

        //when
        UserRoleResponse response = assertDoesNotThrow(() -> userService.changeRole(user.getId(), "ADMIN", auth.getUsername()));

        //then
        assertEquals(response.getMessage(), "ADMIN" + "(으)로 변경되었습니다.");
        assertEquals(response.getUserName(), user.getUsername());
        verify(userRepository).changeRoleToAdmin(user.getId());
    }
    @Test
    @DisplayName("권한 변경 실패 : role 잘못 입력")
    void change_role_fail_잘못입력(){
        User user = UserEntityFixture.get("user", "password");
        User auth = UserEntityFixture.getADMIN("auth", "pw");
        //given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByUserName(auth.getUsername())).willReturn(Optional.of(auth));

        //when
        UserException e = assertThrows(UserException.class,
                () -> userService.changeRole(user.getId(), "잘못입력된값", auth.getUsername()));
        //then
        assertEquals(ErrorCode.INVALID_VALUE, e.getErrorCode());
    }
}