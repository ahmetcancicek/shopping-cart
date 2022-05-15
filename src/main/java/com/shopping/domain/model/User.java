package com.shopping.domain.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"password", "active", "customer"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @Length(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @Column(name = "password", nullable = false)
    @Length(min = 5, message = "Password must have at least 5 characters")
    @NotEmpty(message = "Password must not be empty")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Role> roles = new HashSet<>();

    @Column(name = "active")
    private boolean active = true;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
        return active;
    }
}
