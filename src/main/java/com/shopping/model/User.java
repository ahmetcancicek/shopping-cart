package com.shopping.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email",unique = true,nullable = false)
    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be null")
    private String email;

    @Column(name = "username",unique = true,nullable = false)
    @Length(min = 5,message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be null")
    private String username;

    @Column(name = "password",unique = true,nullable = false)
    @Length(min = 5,message = "Password must have at least 5 characters")
    @NotEmpty(message = "Password must not be null")
    private String password;

    @Column(name = "active",nullable = false)
    private boolean active;

    @OneToOne(mappedBy = "users",cascade = CascadeType.ALL)
    private Customer customer;

    public User(String email,String username,String password,boolean active){
        this.email=email;
        this.username=username;
        this.password=password;
        this.active = active;
    }
}
