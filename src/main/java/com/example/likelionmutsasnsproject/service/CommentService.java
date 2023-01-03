package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.exception.CommentException;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.repository.CommentRepository;
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
    /**댓글 등록**/
    public CommentResponse add(CommentRequest request, Integer postId, String userName) {
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생 -> 알람 기능에서 사용할 예정
        User user = userService.getUserByUserName(userName);    //해당 유저가 없으면 예외 발생
        if(request.getComment().equals("")){        //내용이 없을 경우 예외 발생
            throw new CommentException(ErrorCode.INVALID_VALUE, "댓글 내용을 입력해주세요.");
        }

        Comment saved = commentRepository.save(request.toEntity(post, user));   //댓글 등록
        return CommentResponse.from(saved);
    }
}
