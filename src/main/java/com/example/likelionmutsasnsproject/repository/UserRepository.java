package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE user u SET u.role='ROLE_ADMIN' WHERE u.id=:id", nativeQuery = true)
    void changeRoleToAdmin(@Param(value = "id") Integer id);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE user u SET u.role='ROLE_USER' WHERE u.id=:id", nativeQuery = true)
    void changeRoleToUser(@Param(value = "id") Integer id);
}
