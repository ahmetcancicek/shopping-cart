package com.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "customer")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name",nullable = true)
    private String firstName;

    @Column(name = "last_name",nullable = true)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "users_id")
    private User users;

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
}