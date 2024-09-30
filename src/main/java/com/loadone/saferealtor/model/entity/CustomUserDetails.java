package com.loadone.saferealtor.model.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정: 예를 들어, ROLE_USER와 같은 역할을 반환
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRoleName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // 인코딩된 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUserId();  // 유저 아이디
    }

    // 기타 필요 메서드들 구현 (아래 메서드들은 기본적으로 true로 설정)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // User 객체를 가져오는 메서드
    public User getUser() {
        return this.user;
    }
}
