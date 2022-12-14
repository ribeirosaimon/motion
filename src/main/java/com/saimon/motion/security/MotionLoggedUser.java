package com.saimon.motion.security;

import com.saimon.motion.domain.MotionUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;


@Getter
public class MotionLoggedUser extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 600L;
    private final Object principal;
    private Object credentials;
    private MotionUser.MotionUserRef motionUser;

    public MotionLoggedUser(Object principal, Object credentials) {
        super((Collection) null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public MotionLoggedUser(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static MotionLoggedUser unauthenticated(Object principal, Object credentials) {
        return new MotionLoggedUser(principal, credentials);
    }

    public static MotionLoggedUser authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new MotionLoggedUser(principal, credentials, authorities);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    public void setMotionUser(MotionUser motionUser) {
        this.motionUser = motionUser.getUserRef();
    }
}