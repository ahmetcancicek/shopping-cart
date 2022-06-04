package com.shopping.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "customer")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Payment Type must not be empty")
    @Column(name = "payment_type", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @NotEmpty(message = "Name must not be empty")
    @Column(name = "name")
    private String name;

    @ManyToOne
    private Customer customer;
}
