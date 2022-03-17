package com.shopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Table(name = "orders")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"items", "totalPrice", "payments"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_key", updatable = false, nullable = false, length = 32)
    private String orderKey;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    private Address billingAddress;

    @OneToOne(fetch = FetchType.EAGER)
    private Address shippingAddress;

    @Column(name = "total_price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order")
    private Set<Payment> payments;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderItem> items;

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public void addPayment(Payment payment) {
        if (payments == null)
            payments = new HashSet<>();

        payment.setOrder(this);
        payments.add(payment);

        this.orderStatus = OrderStatus.PAID;
    }
}
