package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByisDeletedFalse(Pageable pageable);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.isDeleted=TRUE, p.deletedAt=:now WHERE p.id=:id")
    void deletePostById(@Param(value="id") Integer id, @Param(value = "now")Timestamp now);
}
