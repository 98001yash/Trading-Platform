package com.company.TradingPlatform.user_service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails userDetails;
    private final String token;

    public JwtAuthenticationToken(UserDetails userDetails, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDetails = userDetails;
        this.token = token;
        setAuthenticated(true); // Mark authentication as successful
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
