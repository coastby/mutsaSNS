package com.example.likelionmutsasnsproject.repository;

import com.example.likelionmutsasnsproject.domain.Alarm;
import com.example.likelionmutsasnsproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByUser(User user, Pageable pageable);
}
