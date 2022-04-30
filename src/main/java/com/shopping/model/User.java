package com.shopping.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"password","active","customer"})
public class User {
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

    @Column(name = "active")
    private boolean active = false;

    @OneToOne(mappedBy = "user")
    private Customer customer;
}
