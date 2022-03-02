package com.shopping.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment_method")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "customer")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Customer customer;
}
