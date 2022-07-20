package com.shopping;

import com.shopping.config.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Testcontainers
public abstract class AbstractIT {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

    public String generateUserToken() {
        return jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Set authorities = new HashSet();
                authorities.add(new SimpleGrantedAuthority("USER"));
                return authorities;
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

    public String generateAdminToken() {
        return jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Set authorities = new HashSet();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                return authorities;
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
                return false;
            }
        });
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> " always");
        registry.add("spring.jpa.defer-datasource-initialization", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.initialization-mode", () -> "never");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
