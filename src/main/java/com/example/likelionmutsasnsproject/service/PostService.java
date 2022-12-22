package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.PostAddRequest;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostWorkResponse add(PostAddRequest request, String userName){
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
        return postRepository.findAll(pageable)
                .map(PostListResponse::from);
    }
    @Transactional
    public PostListResponse getById(Integer id) {
        Post post =  postRepository.findById(id).orElseThrow(()->new PostException(ErrorCode.POST_NOT_FOUND));
        return PostListResponse.from(post);
    }
}
