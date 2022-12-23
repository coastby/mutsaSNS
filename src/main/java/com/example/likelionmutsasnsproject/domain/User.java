package com.example.likelionmutsasnsproject.domain;

import com.example.likelionmutsasnsproject.dto.UserRole;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String password;
    @Column(unique = true)
    private String userName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
