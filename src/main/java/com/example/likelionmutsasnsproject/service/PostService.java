package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.post.PostWorkRequest;
import com.example.likelionmutsasnsproject.dto.post.PostResponse;
import com.example.likelionmutsasnsproject.dto.post.PostWorkResponse;
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

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostWorkResponse add(PostWorkRequest request, String userName){
        //유저가 존재하지 않을 때 등록 실패 -> USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"Not founded")
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, "작성할 수 없는 사용자입니다."));

        //포스트 등록
        Post saved = postRepository.save(request.toEntity(user));

        return PostWorkResponse.builder()
                .message("포스트 등록 완료")
                .postId(saved.getId())
                .build();
    }
    @Transactional
    public Page<PostResponse> getAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponse::from);
    }
    @Transactional
    public PostResponse getById(Integer id) {
        Post post = getPostByPostId(id);
        return PostResponse.from(post);
    }
    public Post getPostByPostId(Integer postId){
        //포스트가 없거나 삭제되었으면 예외 발생
        return postRepository.findById(postId)
                .filter(x -> (!x.isDeleted()))
                .orElseThrow(()->new PostException(ErrorCode.POST_NOT_FOUND));
    }
    // 1) user가 존재하고 2) ADMIN이면 3) 작성자와 유저가 동일하면 true 반환
    public boolean validateUserToPost(Post post, String userName){
        //유저가 존재하지 않을 때 등록 실패 -> USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"Not founded")
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));

        //ADMIN 사용자는 사용 가능
        if(user.getRole().name().equals("ROLE_ADMIN")){
            return true;
        }
        //로그인한 아이디와 게시글의 작성자가 다르면 권한없음
        return userName.equals(post.getUser().getUserName());
    }
    //1) 포스트가 있는지 2) 유저가 권한이 있는지
    @Transactional
    public PostWorkResponse delete(Integer postId, String userName) {
        Post post = getPostByPostId(postId);
        if (!validateUserToPost(post, userName)){   //권한이 없으면 예외발생
            throw new UserException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 포스트만 수정/삭제할 수 있습니다.");
        }
        //쿼리로 isDeleted->true, deletedAt->현재시간 으로 변경
//        postRepository.deletePostById(post.getId(), new Timestamp(System.currentTimeMillis()));
        post.deleteSoftly(new Timestamp(System.currentTimeMillis()));
        return PostWorkResponse.builder()
                .message("포스트 삭제 완료")
                .postId(postId)
                .build();
    }
    @Transactional
    public PostWorkResponse update(Integer postId, PostWorkRequest request, String userName){
        Post post = getPostByPostId(postId);
        if (!validateUserToPost(post, userName)){   //권한이 없으면 예외발생
            throw new UserException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 포스트만 수정/삭제할 수 있습니다.");
        }
        Post saved = postRepository.save(request.editEntity(post));
        return PostWorkResponse.builder()
                .message("포스트 수정 완료")
                .postId(saved.getId())
                .build();
    }
}
