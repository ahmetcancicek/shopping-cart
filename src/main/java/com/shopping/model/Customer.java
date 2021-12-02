package com.shopping.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;

@Table(name = "customer")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User users;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart;

    public Customer(String firstName, String lastName, User users) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.users = users;
    }
}