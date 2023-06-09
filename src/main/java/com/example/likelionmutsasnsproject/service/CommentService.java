package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Alarm;
import com.example.likelionmutsasnsproject.domain.Comment;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.alarrm.AlarmType;
import com.example.likelionmutsasnsproject.dto.comment.CommentRequest;
import com.example.likelionmutsasnsproject.dto.comment.CommentResponse;
import com.example.likelionmutsasnsproject.dto.comment.CommentWorkResponse;
import com.example.likelionmutsasnsproject.exception.CommentException;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final AlarmService alarmService;
    private final PostService postService;
    private final UserService userService;
    /**댓글 조회**/
    @Transactional
    public Page<CommentResponse> getAll(Integer postId, Pageable pageable){
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생
        return commentRepository.findAllByPostId(postId, pageable).map(CommentResponse::from);
    }
    /**댓글 등록**/
    @Transactional
    public CommentResponse add(CommentRequest request, Integer postId, String userName) {
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생
        User user = userService.getUserByUserName(userName);    //해당 유저가 없으면 예외 발생
        if(request.getComment().equals("")){        //내용이 없을 경우 예외 발생
            throw new CommentException(ErrorCode.INVALID_VALUE, "댓글 내용을 입력해주세요.");
        }

        Comment saved = commentRepository.save(request.toEntity(post, user));   //댓글 등록

        if(!user.getId().equals(post.getUser().getId())){   //포스트작성자와 이용자가 다르면 알람 등록
            Alarm alarm = Alarm.makeAlarm(AlarmType.NEW_COMMENT_ON_POST, post, user.getId());
            alarmService.saveAlarm(alarm);
        }
        return CommentResponse.from(saved);
    }
    // 1) user가 존재하고 2) ADMIN이면 3) 작성자와 유저가 동일하면 true 반환
    public boolean validateUserToComment(Comment comment, String userName){
        //유저가 존재하지 않을 때 등록 실패 -> USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"Not founded")
        User user = userService.getUserByUserName(userName);

        //ADMIN 사용자는 사용 가능
        if(user.getRole().name().equals("ROLE_ADMIN")){
            return true;
        }
        //로그인한 아이디와 게시글의 작성자가 다르면 권한없음
        return userName.equals(comment.getUser().getUsername());
    }
    public Comment getCommentById(Integer id){
        return commentRepository.findById(id)    //댓글 아이디에 해당하는 댓글이 없으면 예외 발생
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }
    /**댓글 수정**/
    @Transactional
    public CommentResponse edit(Integer postId, Integer id, CommentRequest request, String userName) {
        if(request.getComment().equals("")){        //내용이 없을 경우 예외 발생
            throw new CommentException(ErrorCode.INVALID_VALUE, "댓글 내용을 입력해주세요.");
        }
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생
        Comment comment = getCommentById(id);
        if(!post.getId().equals(comment.getPost().getId())) {
            throw new CommentException(ErrorCode.COMMENT_NOT_FOUND, "해당 포스트에 있는 댓글이 아닙니다.");}
        if(!validateUserToComment(comment, userName)){
            throw new UserException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 댓글만 수정/삭제할 수 있습니다.");}
        comment.update(request);
        Comment saved = commentRepository.saveAndFlush(comment);
        return CommentResponse.from(saved);
    }
    /**댓글 삭제**/
    @Transactional
    public CommentWorkResponse delete(Integer postId, Integer id, String userName) {
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생
        Comment comment = getCommentById(id);               //댓글 아이디에 해당하는 댓글이 없으면 예외 발생
        if(!post.getId().equals(comment.getPost().getId())) {     //포스트에 있는 댓글이 아니면 예외 발생
            throw new CommentException(ErrorCode.COMMENT_NOT_FOUND, "해당 포스트에 있는 댓글이 아닙니다.");}
        if(!validateUserToComment(comment, userName)){
            throw new UserException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 댓글만 수정/삭제할 수 있습니다.");
        }
        comment.deleteSoftly();
        return CommentWorkResponse.builder()
                .message("댓글 삭제 완료")
                .id(comment.getId())
                .build();
    }
}
