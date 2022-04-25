package com.shopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Table(name = "customer")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PaymentMethod> paymentMethods;

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethods == null) {
            paymentMethods = new HashSet<>();
        }

        paymentMethod.setCustomer(this);
        paymentMethods.add(paymentMethod);
    }

    public void addAddress(Address address) {
        if (addresses == null)
            addresses = new HashSet<>();

        address.setCustomer(this);
        addresses.add(address);
    }
}