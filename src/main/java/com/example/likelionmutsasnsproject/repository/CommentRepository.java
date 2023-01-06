package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByPostId(Integer postId, Pageable pageable);
}
