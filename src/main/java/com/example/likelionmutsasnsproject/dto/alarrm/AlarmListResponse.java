package com.example.likelionmutsasnsproject.dto.alarrm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AlarmListResponse {
    private List<AlarmResponse> content;
}
