package com.example.likelionmutsasnsproject.domain;

import com.example.likelionmutsasnsproject.dto.user.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
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
    private String name;
    private String email;
    private String introduction;
    private String refreshToken;
    public User update(String userName, String email){
        this.userName = userName;
        this.email = email;
        return this;
    }
    public static String getUserNameFromAuthentication(Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return user.getUsername();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }
    @Override
    public String getUsername() {
        return this.userName;
    }
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }
}
