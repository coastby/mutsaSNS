package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Test
    @Transactional
    @DisplayName("좋아요 갯수 세기")
    void count(){
        Post post = Post.builder()
                .id(1)
                .build();
        Integer count = likeRepository.countByPost(post);

        assertEquals(3, count);
    }
//    @Test
//    @Transactional
//    @DisplayName("좋아요 갯수 세기 - 쿼리이용")
//    void count_Query(){
//        Integer count = likeRepository.countByPostQuery(1);
//
//        assertEquals(3, count);
//    }
    @Test
    @Transactional
    @DisplayName("유저와 포스트가 같은 좋아요 있는지 검사")
    void existsByUserAndPost(){
        int result1 = likeRepository.existsByPostAndUser(1, 1);
        int result2 = likeRepository.existsByPostAndUser(1, 3);

        assertEquals(1, result1);
        assertEquals(0, result2);
    }

}