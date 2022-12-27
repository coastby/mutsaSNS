package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserRole;
import com.example.likelionmutsasnsproject.fixture.UserEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("권한 변경 로직")
    void change_role_to_ADMIN(){
        User user = UserEntityFixture.get("user", "password");
        User before = userRepository.findById(user.getId()).orElse(null);
        assert before != null;
        assertEquals(UserRole.ROLE_USER, before.getRole()); //현재 권한 USER

        //when
        userRepository.changeRoleToAdmin(user.getId());
        //then
        User saved = userRepository.findById(user.getId()).orElse(null);

        assert saved != null;
        assertEquals(UserRole.ROLE_ADMIN, saved.getRole()); //바뀐 권한 ADMIN
    }
}