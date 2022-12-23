package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.PostWorkRequest;
import com.example.likelionmutsasnsproject.dto.PostListResponse;
import com.example.likelionmutsasnsproject.dto.PostWorkResponse;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.PostException;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.PostRepository;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostWorkResponse add(PostWorkRequest request, String userName){
        //유저가 존재하지 않을 때 등록 실패 -> USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"Not founded")
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, "작성할 수 없는 사용자입니다."));

        //포스트 등록
        Post saved = postRepository.save(request.toEntity(user));

        return PostWorkResponse.builder()
                .message("포스트 등록 완료")
                .postId(saved.getId())
                .build();
    }
    @Transactional
    public Page<PostListResponse> getAll(Pageable pageable) {
        return postRepository.findByisDeletedFalse(pageable)
                .map(PostListResponse::from);
    }
    @Transactional
    public PostListResponse getById(Integer id) {
        Post post =  postRepository.findById(id)
                .filter(x -> (!x.isDeleted()))
                .orElseThrow(()->new PostException(ErrorCode.POST_NOT_FOUND));
        return PostListResponse.from(post);
    }

    //1) 포스트가 있는지 2) 유저가 있는지 3) 작성자와 유저가 동일한지 체크 후 user 반환
    public User passWithUserAndPost(Integer postId, String userName){
        //포스트가 없거나 삭제되었으면 예외 발생
        Post post =  postRepository.findById(postId)
                .filter(x -> (!x.isDeleted()))
                .orElseThrow(()->new PostException(ErrorCode.POST_NOT_FOUND));

        //유저가 존재하지 않을 때 등록 실패 -> USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"Not founded")
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));

        //로그인한 아이디와 게시글의 작성자가 다르면 권한없음 발생
        if(!userName.equals(post.getUser().getUserName())){
            throw new UserException(ErrorCode.INVALID_PERMISSION);
        }
        return user;
    }
    @Transactional
    public PostWorkResponse delete(Integer postId, String userName) {
        User user = passWithUserAndPost(postId, userName);
        //쿼리로 isDeleted->true, deletedAt->현재시간 으로 변경
        postRepository.deletePostById(postId, new Timestamp(System.currentTimeMillis()));
        return PostWorkResponse.builder()
                .message("포스트 삭제 완료")
                .postId(postId)
                .build();
    }
    @Transactional
    public PostWorkResponse update(Integer postId, PostWorkRequest request, String userName){
        User user = passWithUserAndPost(postId, userName);
        postRepository.save(request.toEntity(postId, user));
        return PostWorkResponse.builder()
                .message("포스트 수정 완료")
                .postId(postId)
                .build();
    }
}
