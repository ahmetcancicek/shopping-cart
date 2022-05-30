package com.shopping.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
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
        if (items == null)
            return Optional.empty();

        return items.stream()
                .filter(cartItem -> Objects.equals(cartItem.getProduct(), product))
                .findFirst();
    }

    public Optional<CartItem> findItemBySerialNumber(String serialNumber) {
        if (items == null)
            return Optional.empty();

        return items.stream()
                .filter(cartItem -> Objects.equals(cartItem.getProduct().getSerialNumber(), serialNumber))
                .findFirst();
    }

    public BigDecimal findTotalPrice() {
        return items.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int findTotalQuantity() {
        return items.stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
    }

    public void removeItem(CartItem item) {
        if (items != null)
            items.removeIf(cartItem -> Objects.equals(cartItem.getId(), item.getId()));
    }

    public void updateItem(CartItem item) {
        removeItem(item);
        addItem(item);
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
