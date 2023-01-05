package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import com.example.likelionmutsasnsproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
//    @Modifying(clearAutomatically = true)  -> @Where 쓰면서 사용x
//    @Query("UPDATE Post p SET p.isDeleted=TRUE, p.deletedAt=:now WHERE p.id=:id")
//    void deletePostById(@Param(value="id") Integer id, @Param(value = "now")Timestamp now);
    Page<Post> findAllByUser(User user, Pageable pageable);
}
