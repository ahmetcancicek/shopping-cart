package com.shopping.domain.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @NotEmpty(message = "Role name must not be empty")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
