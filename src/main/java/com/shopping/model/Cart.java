package com.shopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "items")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total_price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    private BigDecimal totalPrice;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CartItem> items;

    public Cart(Customer customer, BigDecimal totalPrice) {
        this.customer = customer;
        this.totalPrice = totalPrice;
    }

    public void addItem(CartItem item) {
        if (items == null)
            items = new HashSet<>();

        items.add(item);
    }

    public Optional<CartItem> findItem(Product product) {
        if (items==null)
            return Optional.empty();

        // TODO: Return value
        return null;
    }

    public void removeItem(CartItem item) {
        // TODO: Method body
    }

    public void updateItem(CartItem item) {
        // TODO: Method body
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
