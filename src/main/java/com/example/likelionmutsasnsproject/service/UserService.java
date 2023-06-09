package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.user.*;
import com.example.likelionmutsasnsproject.exception.ErrorCode;
import com.example.likelionmutsasnsproject.exception.UserException;
import com.example.likelionmutsasnsproject.repository.UserRepository;
import com.example.likelionmutsasnsproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        //아이디 중복 시 예외 발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new UserException(ErrorCode.DUPLICATED_USER_NAME);
                });
        //비밀 번호 인코딩해서 DB 저장록
        User saved = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserJoinResponse.from(saved);
    }
    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        //아이디가 존재하는지 확인
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, "아이디가 틀렸습니다."));

        //비밀번호가 일치하는지 확인
        if(!encoder.matches(request.getPassword(), user.getPassword())){
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new UserLoginResponse(token);
    }
    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));
    }

    public UserRoleResponse changeRole(Integer userId, String role, String userName) {
        User user = userRepository.findById(userId)             //PathVariable의 사용자 아이디가 있는 지 확인
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));
        User auth = userRepository.findByUserName(userName)     //로그인한 사용자이름이 있는지 확인
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND));

        //로그인한 유저가 ADMIN이 맞는지 확인 (security에서 했는데 한 번 더 확인)
        if (!auth.getRole().name().equals("ROLE_ADMIN")){
            throw new UserException(ErrorCode.INVALID_PERMISSION, "ADMIN 유저만 이용가능합니다.");
        }

        //현재 권한과 바꾸려는 권한이 다르면 변경
        if(!user.getRole().name().equals("ROLE_"+role)){
            if(role.equals("ADMIN")){
                userRepository.changeRoleToAdmin(user.getId());
            } else if(role.equals("USER")){
                userRepository.changeRoleToUser(user.getId());
            } else {
                throw new UserException(ErrorCode.INVALID_VALUE, "ADMIN, USER 중 하나를 입력해주세요.");
            }
        }
        return new UserRoleResponse(role+"(으)로 변경되었습니다.", user.getUsername());
    }
    @Transactional
    public UserResponse getMyInfo(String username) {
        User user = getUserByUserName(username);
        return UserResponse.from(user);
    }
}
