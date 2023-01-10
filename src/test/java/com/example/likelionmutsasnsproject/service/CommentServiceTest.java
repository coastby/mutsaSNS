package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.BaseEntity;
import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.dto.comment.CommentWorkResponse;
import com.example.likelionmutsasnsproject.exception.CommentException;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.fixture.CommentEntityFixture;
import com.example.likelionmutsasnsproject.fixture.PostEntityFixture;
import com.example.likelionmutsasnsproject.fixture.TestInfoFixture;
import com.example.likelionmutsasnsproject.fixture.UserEntityFixture;
import com.example.likelionmutsasnsproject.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CommentServiceTest {
    private CommentService commentService;
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final AlarmService alarmService = mock(AlarmService.class);
    private final UserService userService = mock(UserService.class);
    private final PostService postService = mock(PostService.class);

    @BeforeEach
    void setUp(){
        commentService = new CommentService(commentRepository, alarmService, postService, userService);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
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
    @Nested
    @DisplayName("댓글 수정")
    class edit{
        Post targetPost = PostEntityFixture.get("user", "pw", false);
        @Test
        @DisplayName("댓글 수정 성공")
        void edit_success(){
            CommentRequest request = new CommentRequest("바뀐 댓글");
            Comment comment = CommentEntityFixture.get("user", "pw", false);
            Comment saved = Comment.builder().id(comment.getId()).post(comment.getPost()).user(comment.getUser())
                    .comment(request.getComment()).build();
            ReflectionTestUtils.setField(
                    saved,
                    BaseEntity.class,
                    "updatedAt",
                    new Timestamp(System.currentTimeMillis()),
                    Timestamp.class
            );
            ReflectionTestUtils.setField(
                    saved,
                    BaseEntity.class,
                    "createdAt",
                    comment.getCreatedAt(),
                    Timestamp.class
            );

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("user")).willReturn(UserEntityFixture.get("user", "pw"));
            given(commentRepository.saveAndFlush(any())).willReturn(saved);

            CommentResponse response =
                    assertDoesNotThrow(() -> commentService.edit(1, 1, request, "user"));
            assertEquals(1, response.getId());
            assertEquals(1, response.getPostId());
            assertEquals("user", response.getUserName());
            assertEquals("바뀐 댓글", response.getComment());
            assertNotNull(response.getCreatedAt());
            assertNotNull(response.getLastModifiedAt());
        }
        @Test
        @DisplayName("댓글 수정 실패 - 포스트 존재하지 않음")
        void edit_fail_포스트없음(){
            CommentRequest request = new CommentRequest("바뀐 댓글");

            given(postService.getPostByPostId(1)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

            PostException e =
                    assertThrows(PostException.class, () -> commentService.edit(1, 1, request, "user"));
            assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        }
        @Test
        @DisplayName("댓글 수정 실패 - 작성자 불일치")
        void edit_fail_작성자불일치(){
            CommentRequest request = new CommentRequest("바뀐 댓글");
            Comment comment = CommentEntityFixture.get("user", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("NotAuthor")).willReturn(UserEntityFixture.get("NotAuthor", "pw"));

            UserException e =
                    assertThrows(UserException.class, () -> commentService.edit(1, 1, request, "NotAuthor"));
            assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        }
        @Test
        @DisplayName("댓글 수정 실패 - 유저 존재하지 않음")
        void edit_fail_유저없음(){
            CommentRequest request = new CommentRequest("바뀐 댓글");
            Comment comment = CommentEntityFixture.get("user", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("NotUser")).willThrow(new UserException(ErrorCode.USERNAME_NOT_FOUND));

            UserException e =
                    assertThrows(UserException.class, () -> commentService.edit(1, 1, request, "NotUser"));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
        }
    }
    @Nested
    @DisplayName("댓글 삭제")
    class delete{
        Post targetPost = PostEntityFixture.get("user", "pw", false);
        @Test
        @DisplayName("댓글 삭제 성공")
        void delete_success(){
            Comment comment = CommentEntityFixture.get("user", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("user")).willReturn(UserEntityFixture.get("user", "pw"));

            CommentWorkResponse response =
                    assertDoesNotThrow(() -> commentService.delete(1, 1, "user"));
            assertEquals("댓글 삭제 완료", response.getMessage());
            assertEquals(1, response.getId());
        }
        @Test
        @DisplayName("댓글 삭제 실패 - 포스트 존재하지 않음")
        void delete_fail_포스트없음(){
            CommentRequest request = new CommentRequest("바뀐 댓글");

            given(postService.getPostByPostId(1)).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));

            PostException e =
                    assertThrows(PostException.class, () -> commentService.delete(1, 1, "user"));
            assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        }
        @Test
        @DisplayName("댓글 삭제 실패 - 댓글 존재하지 않음")
        void delete_fail_댓글없음(){
            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.empty());

            CommentException e =
                    assertThrows(CommentException.class, () -> commentService.delete(1, 1, "user"));
            assertEquals(ErrorCode.COMMENT_NOT_FOUND, e.getErrorCode());
        }
        @Test
        @DisplayName("댓글 삭제 실패 - 작성자 불일치")
        void delete_fail_작성자불일치(){
            CommentRequest request = new CommentRequest("바뀐 댓글");
            Comment comment = CommentEntityFixture.get("user", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("NotAuthor")).willReturn(UserEntityFixture.get("NotAuthor", "pw"));

            UserException e =
                    assertThrows(UserException.class, () -> commentService.delete(1, 1, "NotAuthor"));
            assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        }
        @Test
        @DisplayName("댓글 수정 실패 - 유저 존재하지 않음")
        void delete_fail_유저없음(){
            Comment comment = CommentEntityFixture.get("user", "pw", false);

            given(postService.getPostByPostId(1)).willReturn(targetPost);
            given(commentRepository.findById(1)).willReturn(Optional.of(comment));
            given(userService.getUserByUserName("NotUser")).willThrow(new UserException(ErrorCode.USERNAME_NOT_FOUND));

            UserException e =
                    assertThrows(UserException.class, () -> commentService.delete(1, 1, "NotUser"));
            assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
        }
    }
}