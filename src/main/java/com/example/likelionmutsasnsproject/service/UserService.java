package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.UserJoinRequest;
import com.example.likelionmutsasnsproject.dto.UserJoinResponse;
import com.example.likelionmutsasnsproject.dto.UserLoginRequest;
import com.example.likelionmutsasnsproject.dto.UserLoginResponse;
import com.example.likelionmutsasnsproject.exception.UserErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

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
        //아이디가 존재하는지 확인
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UserException(UserErrorCode.USERNAME_NOT_FOUND, "아이디가 틀렸습니다."));

        //비밀번호가 일치하는지 확인
        if(!encoder.matches(request.getPassword(), user.getPassword())){
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성
        String token = jwtUtil.generateToken(user.getUserName(), user.getRole());

        return new UserLoginResponse(token);
    }
}
