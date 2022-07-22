package com.shopping.integration.controller;

import com.shopping.common.EcommerceStarterMySQLApplicationContainer;
import com.shopping.config.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Testcontainers
public abstract class AbstractIT {

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    protected Integer port;

    protected HttpHeaders headers = new HttpHeaders();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Container
    private static EcommerceStarterMySQLApplicationContainer container = EcommerceStarterMySQLApplicationContainer.getInstance();

    protected String generateUserToken() {
        return jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return new HashSet<>(Set.of(new SimpleGrantedAuthority("USER")));
            }

            @Override
            public String getPassword() {
                return "DHN827D9N";
            }

            @Override
            public String getUsername() {
                return "lucycar";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
    }

    protected String generateAdminToken() {
        return jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return new HashSet<>(Set.of(new SimpleGrantedAuthority("ADMIN")));

            }

            @Override
            public String getPassword() {
                return "DHN827D9N";
            }

            @Override
            public String getUsername() {
                return "billking";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
    }
}
