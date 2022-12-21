package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserJoinRequest;
import com.example.likelionmutsasnsproject.dto.UserJoinResponse;
import com.example.likelionmutsasnsproject.dto.UserLoginRequest;
import com.example.likelionmutsasnsproject.dto.UserLoginResponse;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserJoinResponse join(UserJoinRequest request) {
        //아이디 중복 시 예외 발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new UserException(UserErrorCode.DUPLICATED_USER_NAME);
                });
        //비밀 번호 인코딩해서 DB 저장
        User saved = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserJoinResponse.from(saved);
    }

    public UserLoginResponse login(UserLoginRequest request) {
        return new UserLoginResponse("token");
    }
}
