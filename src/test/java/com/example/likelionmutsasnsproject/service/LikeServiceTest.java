package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.LikeException;
import com.example.likelionmutsasnsproject.fixture.PostEntityFixture;
import com.example.likelionmutsasnsproject.fixture.TestInfoFixture;
import com.example.likelionmutsasnsproject.fixture.UserEntityFixture;
import com.example.likelionmutsasnsproject.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LikeServiceTest {
    private LikeService likeService;
    private LikeRepository likeRepository = mock(LikeRepository.class);
    private UserService userService = mock(UserService.class);
    private PostService postService = mock(PostService.class);
    private TestInfoFixture.TestInfo fixture;

    @BeforeEach
    void setUp(){
        likeService = new LikeService(likeRepository, postService, userService);
        fixture = TestInfoFixture.get();
    }

    @Nested
    @DisplayName("좋아요")
    class addLike{
        @Test
        @DisplayName("좋아요 실패 - 이미 완료된 처리")
        void add_fail_중복(){
            User user = UserEntityFixture.get("user", "pw");
            Post post = PostEntityFixture.get("author", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(post);
            given(userService.getUserByUserName("user")).willReturn(user);
            given(likeRepository.existsByPostAndUser(post.getId(), user.getId())).willReturn(1);

            LikeException e = assertThrows(LikeException.class, () -> {likeService.add(1, "user");});

            assertEquals(ErrorCode.INVALID_REQUEST, e.getErrorCode());
        }
    }

}