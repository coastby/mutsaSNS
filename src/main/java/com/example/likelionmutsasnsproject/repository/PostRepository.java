package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
//    @Modifying(clearAutomatically = true)  -> @Where 쓰면서 사용x
//    @Query("UPDATE Post p SET p.isDeleted=TRUE, p.deletedAt=:now WHERE p.id=:id")
//    void deletePostById(@Param(value="id") Integer id, @Param(value = "now")Timestamp now);
    Page<Post> findAllByUser(User user, Pageable pageable);
}
