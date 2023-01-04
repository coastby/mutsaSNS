package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.post.PostResponse;
import com.example.likelionmutsasnsproject.dto.post.PostWorkRequest;
import com.example.likelionmutsasnsproject.dto.post.PostWorkResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.fixture.PostEntityFixture;
import com.example.likelionmutsasnsproject.fixture.TestInfoFixture;
import com.example.likelionmutsasnsproject.fixture.UserEntityFixture;
import com.example.likelionmutsasnsproject.repository.PostRepository;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;

class PostServiceTest {
    private PostService postService;
    private PostRepository postRepository = mock(PostRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private TestInfoFixture.TestInfo fixture;

    @BeforeEach
    void setUp(){
        postService = new PostService(postRepository, userRepository);
        fixture = TestInfoFixture.get();
    }
    /**
     * 포스트 등록 테스트
     * **/

    @Test
    @DisplayName("등록 성공")
    void add_success(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);
        User mockUserEntity = mock(User.class);

        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.of(mockUserEntity));
        given(postRepository.save(any())).willReturn(mockPostEntity);
        //when
        PostWorkResponse response = postService.add(request, fixture.getUserName());
        //then
        assertDoesNotThrow(() -> postService.add(request, fixture.getUserName()));
        assertEquals(response.getMessage(), "포스트 등록 완료");
    }
    @Test
    @DisplayName("등록 실패 - 유저가 존재하지 않을 때")
    void add_fail_유저없음(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);

        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.empty());
        given(postRepository.save(any())).willReturn(mockPostEntity);

        UserException e = assertThrows(UserException.class, () -> {postService.add(request, fixture.getUserName());});
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
    }
    /**
     * 포스트 상세 조회 테스트
     * **/
    @Test
    @DisplayName("상세 조회 성공")
    void show_success(){
        Integer postId = fixture.getPostId();
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);

        given(postRepository.findById(postId)).willReturn(Optional.of(mockPostEntity));
        //when
        PostResponse response = postService.getById(postId);
        //then
        assertEquals(postId, response.getId());
        assertEquals(fixture.getUserName(), response.getUserName());
        assertEquals(fixture.getTitle(), response.getTitle());
        assertNotNull(response.getCreatedAt());
    }
    @Test
    @DisplayName("상세 조회 실패 - 삭제된 게시글")
    void show_fail_삭제(){
        Integer postId = fixture.getPostId();
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), true);
        given(postRepository.findById(postId)).willReturn(Optional.of(mockPostEntity));

        //when
        PostException e = assertThrows(PostException.class, () -> {postService.getById(postId);});
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    @Test
    @DisplayName("상세 조회 실패 - 없는 게시글")
    void show_fail_게시글없음(){
        Integer postId = fixture.getPostId();
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        //when
        PostException e = assertThrows(PostException.class, () -> {postService.getById(postId);});
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    /**
     * 포스트 수정 테스트
     * **/
    @Test
    @DisplayName("수정 성공")
    void edit_success(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);
        User mockUserEntity = UserEntityFixture.get("user", "password");

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.of(mockUserEntity));
        given(postRepository.save(any())).willReturn(mockPostEntity);
        //when
        PostWorkResponse response = assertDoesNotThrow(() -> postService.update(fixture.getPostId(), request, fixture.getUserName()));
        //then
        assertEquals(response.getMessage(), "포스트 수정 완료");
    }
    @Test
    @DisplayName("수정 실패 - 유저가 작성자가 아닐 떄")
    void edit_fail_유저불일치(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);
        User mockUserEntity = UserEntityFixture.get("non_author", "password");

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName("non_author")).willReturn(Optional.of(mockUserEntity));
        given(postRepository.save(any())).willReturn(mockPostEntity);
        //when
        UserException e = assertThrows(UserException.class,
                () -> {postService.update(fixture.getPostId(), request, "non_author");});
        //then
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
    @Test
    @DisplayName("수정 실패 - 유저가 존재하지 않음")
    void edit_fail_유저없음(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName("non")).willThrow(new UserException(ErrorCode.USERNAME_NOT_FOUND));
        given(postRepository.save(any())).willReturn(mockPostEntity);
        //when
        UserException e = assertThrows(UserException.class,
                () -> {postService.update(fixture.getPostId(), request, "non");});
        //then
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
    }
    @Test
    @DisplayName("수정 실패 - 포스트가 존재하지 않음")
    void edit_fail_포스트없음(){
        PostWorkRequest request = new PostWorkRequest(fixture.getTitle(), fixture.getBody());
        User mockUserEntity = UserEntityFixture.get("user", "password");


        given(postRepository.findById(fixture.getPostId())).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));
        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.of(mockUserEntity));
        //when
        PostException e = assertThrows(PostException.class,
                () -> {postService.update(fixture.getPostId(), request, "user");});
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    /**
     * 포스트 삭제 테스트
     * **/
    @Test
    @DisplayName("삭제 성공")
    void delete_success(){
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);
        User mockUserEntity = UserEntityFixture.get("user", "password");

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.of(mockUserEntity));
//        willDoNothing().given(postRepository).deletePostById(fixture.getPostId(), new Timestamp(System.currentTimeMillis()));
        //when
        PostWorkResponse response = assertDoesNotThrow(() -> postService.delete(fixture.getPostId(), fixture.getUserName()));
        //then
        assertEquals(response.getMessage(), "포스트 삭제 완료");
    }
    @Test
    @DisplayName("삭제 실패 - 유저가 작성자가 아닐 떄")
    void delete_fail_유저불일치(){
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);
        User mockUserEntity = UserEntityFixture.get("non_author", "password");

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName("non_author")).willReturn(Optional.of(mockUserEntity));
//        willDoNothing().given(postRepository).deletePostById(fixture.getPostId(), new Timestamp(System.currentTimeMillis()));

        //when
        UserException e = assertThrows(UserException.class,
                () -> {postService.delete(fixture.getPostId(), "non_author");});
        //then
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
    @Test
    @DisplayName("삭제 실패 - 유저가 존재하지 않음")
    void delete_fail_유저없음(){
        Post mockPostEntity = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword(), false);

        given(postRepository.findById(fixture.getPostId())).willReturn(Optional.of(mockPostEntity)) ;
        given(userRepository.findByUserName("non")).willThrow(new UserException(ErrorCode.USERNAME_NOT_FOUND));
//        willDoNothing().given(postRepository).deletePostById(fixture.getPostId(), new Timestamp(System.currentTimeMillis()));

        //when
        UserException e = assertThrows(UserException.class,
                () -> {postService.delete(fixture.getPostId(), "non");});
        //then
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, e.getErrorCode());
    }
    @Test
    @DisplayName("삭제 실패 - 포스트가 존재하지 않음")
    void delete_fail_포스트없음(){
        User mockUserEntity = UserEntityFixture.get("user", "password");

        given(postRepository.findById(fixture.getPostId())).willThrow(new PostException(ErrorCode.POST_NOT_FOUND));
        given(userRepository.findByUserName(fixture.getUserName())).willReturn(Optional.of(mockUserEntity));
//        willDoNothing().given(postRepository).deletePostById(fixture.getPostId(), new Timestamp(System.currentTimeMillis()));
        //when
        PostException e = assertThrows(PostException.class,
                () -> {postService.delete(fixture.getPostId(), "user");});
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
}
