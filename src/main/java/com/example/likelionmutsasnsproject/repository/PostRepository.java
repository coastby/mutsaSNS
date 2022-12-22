package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
