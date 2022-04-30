package com.shopping.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serial_number", nullable = false, unique = true)
    @Length(min = 3, message = "Serial number have at least 3 characters")
    @NotEmpty(message = "Serial number must not be empty")
    private String serialNumber;

    @Column(name = "name", nullable = false)
    @Length(min = 3, message = "Name must have at least 5 characters")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.00", message = "Price must not to be negative number")
    public BigDecimal price;

    @Column(name = "quantity")
    @Min(value = 0, message = "Quantity must not to be negative number")
    public Integer quantity;
}
