package com.example.likelionmutsasnsproject.dto;

import com.example.likelionmutsasnsproject.domain.Alarm;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@Builder
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private Integer fromUserId;
    private Integer targetId;
    private String text;
    private String createdAt;

    public static AlarmResponse from (Alarm alarm){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getAlarmType().getText())
                .createdAt(dateFormat.format(alarm.getCreatedAt()))
                .build();
    }
}
