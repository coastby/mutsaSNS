package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
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
    public Page<CommentResponse> getAll(Integer postId, Pageable pageable){
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생 -> 알람 기능에서 사용할 예정
        return commentRepository.findAllByPostId(postId, pageable).map(CommentResponse::from);
    }
}
