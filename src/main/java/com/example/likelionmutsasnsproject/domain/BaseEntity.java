package com.example.likelionmutsasnsproject.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@ToString
@MappedSuperclass   //BaseEntity를 상속한 entity들은 BaseEntity의 멤버변수를 모두 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)  //entity를 DB에 적용 전후로 콜백(auditing 정보를 주입하는 클래스)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private Timestamp createdAt;
    @LastModifiedDate
    private Timestamp updatedAt;
}
