package com.shopping.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private PaymentMethod paymentMethod;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.00", message = "Amount must not to be negative number")
    private BigDecimal amount;

    @Column(name = "completed")
    private boolean completed = false;

    @Column(name = "completed_time")
    private LocalDateTime completedTime = LocalDateTime.now();

    @Column(name = "payment_confirmation", length = 32)
    private String confirmation;

    @ManyToOne
    private Address billingAddress;

    @OneToOne
    private Order order;
}
