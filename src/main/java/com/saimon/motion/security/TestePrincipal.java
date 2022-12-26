package com.saimon.motion.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;


public class TestePrincipal extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 600L;
    private final Object principal;
    private Object credentials;

    public TestePrincipal(Object principal, Object credentials) {
        super((Collection)null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public TestePrincipal(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static TestePrincipal unauthenticated(Object principal, Object credentials) {
        return new TestePrincipal(principal, credentials);
    }

    public static TestePrincipal authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new TestePrincipal(principal, credentials, authorities);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}