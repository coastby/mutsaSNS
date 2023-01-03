package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.CommentRequest;
import com.example.likelionmutsasnsproject.dto.CommentResponse;
import com.example.likelionmutsasnsproject.repository.CommentRepository;
import com.example.likelionmutsasnsproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    public Page<CommentResponse> getAll(Integer postId, Pageable pageable){
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생
        return commentRepository.findAllByPostId(postId, pageable).map(CommentResponse::from);
    }
    //댓글 등록
    public CommentResponse add(CommentRequest request, Integer postId, String userName) {
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생 -> 알람 기능에서 사용할 예정
        User user = userService.getUserByUserName(userName);    //해당 유저가 없으면 예외 발생

        //댓글 등록
        Comment saved = commentRepository.save(request.toEntity(post, user));
        return CommentResponse.from(saved);
    }
}
