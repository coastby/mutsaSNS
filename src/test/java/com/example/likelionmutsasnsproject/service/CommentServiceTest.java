package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.exception.CommentException;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.fixture.TestInfoFixture;
import com.example.likelionmutsasnsproject.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CommentServiceTest {
    private CommentService commentService;
    private CommentRepository commentRepository = mock(CommentRepository.class);
    private UserService userService = mock(UserService.class);
    private PostService postService = mock(PostService.class);
    private TestInfoFixture.TestInfo fixture;

    @BeforeEach
    void setUp(){
        commentService = new CommentService(commentRepository, postService, userService);
        fixture = TestInfoFixture.get();
    }
    @Nested
    @DisplayName("댓글 등록")
    class add{
        @Test
        @DisplayName("등록 실패 - 댓글 내용 없음")
        void add_fail_내용무(){
            CommentRequest request = new CommentRequest("");

            given(postService.getPostByPostId(1)).willReturn(new Post());
            given(userService.getUserByUserName("user")).willReturn(new User());

            //when
            CommentException e = assertThrows(CommentException.class, () -> {commentService.add(request,1, "user");});
            //then
            assertEquals(ErrorCode.INVALID_VALUE, e.getErrorCode());
        }
    }
}