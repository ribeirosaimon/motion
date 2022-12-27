package com.saimon.motion.security;

import com.saimon.motion.domain.MotionUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class MotionPrincipal implements UserDetails {

    private final MotionUser motionUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        motionUser.getRoles().forEach(s -> authorities.add(new SimpleGrantedAuthority(s.getName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return motionUser.getPassword();
    }

    @Override
    public String getUsername() {
        return motionUser.getUsername();
    }

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
}
