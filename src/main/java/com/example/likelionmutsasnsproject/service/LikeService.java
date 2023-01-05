package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Like;
import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.LikeException;
import com.example.likelionmutsasnsproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;

    /**좋아요 추가**/
    public String add(Integer postId, String userName){
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생 -> 알람 기능에서 사용할 예정
        User user = userService.getUserByUserName(userName);    //해당 유저가 없으면 예외 발생

        if(likeRepository.existsByPostAndUser(post.getId(), user.getId()) == 1){
            throw new LikeException(ErrorCode.INVALID_REQUEST, "좋아요는 한 번만 할 수 있습니다.");
        }
        Like saved = likeRepository.save(Like.makeLike(post, user));
        return "좋아요를 눌렀습니다.";
    }

    public Integer getCount(Integer postId) {
        Post post = postService.getPostByPostId(postId);    //포스트가 없거나 삭제되었으면 예외 발생 -> 알람 기능에서 사용할 예정
        return likeRepository.countByPost(post);
    }
}
