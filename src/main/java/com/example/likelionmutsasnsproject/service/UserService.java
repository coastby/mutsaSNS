package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserJoinRequest;
import com.example.likelionmutsasnsproject.dto.UserJoinResponse;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserJoinResponse join(UserJoinRequest request) {
        //아이디 중복 시 예외 발생

        //비밀 번호 인코딩



        User saved = userRepository.save(request.toEntity());
        return UserJoinResponse.from(saved);

    }
}
