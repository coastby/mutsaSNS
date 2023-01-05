package com.example.likelionmutsasnsproject.domain;

import com.example.likelionmutsasnsproject.dto.alarrm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is NULL")
public class Alarm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType alarmType;
    @Column(nullable = false)
    private Integer fromUserId;
    @Column(nullable = false)
    private Integer targetId;

    public static Alarm makeAlarm(AlarmType alarmType, Post post, Integer fromUserId){
        return Alarm.builder()
                .user(post.getUser())
                .alarmType(alarmType)
                .fromUserId(fromUserId)
                .targetId(post.getId())
                .build();
    }
}
