package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Like;
import com.example.likelionmutsasnsproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    @Query(value = "SELECT EXISTS (SELECT * FROM heart h WHERE h.post_id = :postId AND h.user_id = :userId)"
            , nativeQuery = true)
    int existsByPostAndUser(Integer postId, Integer userId);
    Integer countByPost(Post post);
//    @Query(value = "SELECT COUNT(id) FROM heart WHERE post_id = :postId", nativeQuery = true)
//    Integer countByPostQuery(Integer postId);

}
