package com.example.likelionmutsasnsproject.service;

import com.example.likelionmutsasnsproject.domain.Alarm;
import com.example.likelionmutsasnsproject.domain.User;
import com.example.likelionmutsasnsproject.dto.alarrm.AlarmResponse;
import com.example.likelionmutsasnsproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserService userService;

    /**알람 조회**/
    public Page<AlarmResponse> getAlarms(String userName, Pageable pageable) {
        User user = userService.getUserByUserName(userName); //user 없으면 예외 발생
        return alarmRepository.findAllByUser(user, pageable)
                .map(AlarmResponse::from);
    }
    /**알람 등록**/
    public void saveAlarm(Alarm alarm){
        Alarm saved = alarmRepository.save(alarm);
    }


}
