package com.example.likelionmutsasnsproject.controller;

import com.example.likelionmutsasnsproject.dto.alarrm.AlarmListResponse;
import com.example.likelionmutsasnsproject.dto.alarrm.AlarmResponse;
import com.example.likelionmutsasnsproject.dto.Response;
import com.example.likelionmutsasnsproject.service.AlarmService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
@Api(tags = "알람")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public Response<AlarmListResponse> getAlarms(Authentication authentication,
                                                 @ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        String userName = authentication.getPrincipal().toString();
        Page<AlarmResponse> alarms = alarmService.getAlarms(userName, pageable);
        return Response.success(new AlarmListResponse(alarms.getContent()));
    }
}
