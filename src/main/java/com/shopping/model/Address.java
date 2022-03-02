package com.shopping.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "street", nullable = false)
    @Length(min = 3, message = "Street must have at least 3 characters")
    @NotEmpty(message = "Street must not be empty")
    private String street;

    @Column(name = "city", nullable = false)
    @Length(min = 3, message = "City must have at least 3 characters")
    @NotEmpty(message = "City must not be empty")
    private String city;

    @Column(name = "zipCode")
    @NotEmpty(message = "Zip Code must not be empty")
    @Length(min = 3, message = "Zip Code  must have at least 3 characters")
    private String zipCode;

    @Column(name = "stateCode", length = 2)
    @NotEmpty(message = "State Code must not be empty")
    @Length(min = 2, max = 2, message = "State Code must have 2 characters")
    private String stateCode;

    @Column(name = "is_primary")
    private boolean primary = false;

    @ManyToOne
    private Customer customer;
}
