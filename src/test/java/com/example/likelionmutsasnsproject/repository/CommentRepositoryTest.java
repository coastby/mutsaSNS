package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp(){
    }
    @Test
    @Transactional
    @DisplayName("save")
    void save(){
        Comment saved = commentRepository.save(Comment.builder()
                        .post(new Post())
                        .user(new User())
                        .comment("안녕")
                        .build());

//        assertNotNull(saved.getCreatedAt());
        log.info("createdAt : {}", saved.getCreatedAt());
        assertNotNull(saved.getId());
    }
    @Test
    @Transactional
    @DisplayName("findById")
    void findById(){
        Comment comment = commentRepository.findById(1).orElse(null);
        Optional<Comment> optionalComment= commentRepository.findById(3);

        assertEquals(1, comment.getId());
        assertNotNull(comment.getCreatedAt());
        assertTrue(optionalComment.isEmpty());

    }

}