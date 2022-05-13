package com.shopping.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "cart")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Min(value = 0, message = "Quantity must not to be negative number")
    private Integer quantity;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    private BigDecimal price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public CartItem(Product product, Integer quantity, BigDecimal price, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.cart = cart;
    }
}
