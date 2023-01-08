package com.example.likelionmutsasnsproject.domain;

import com.example.likelionmutsasnsproject.dto.user.UserRole;
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
    @Column(unique = true, nullable = false)
    private String userName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    /**OAuth2 적용**/
    private String oauthId;
    private String email;
//    private String imgUrl;
    private String introduction;
    private String refreshToken;
    public User update(String userName, String email){
        this.userName = userName;
        this.email = email;
        return this;
    }
}
