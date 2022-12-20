package com.example.likelionmutsasnsproject.domain;

import com.example.likelionmutsasnsproject.dto.UserRole;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
