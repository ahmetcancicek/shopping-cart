package com.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    @Length(min = 3,message = "Name must have at least 5 characters")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price",nullable = false)
    @DecimalMin(value = "0.00",message = "Price must not to be negative number")
    public BigDecimal price;

    @Column(name = "quantity")
    @Min(value = 0, message = "Quantity must not to be negative number")
    public Integer quantity;
}
