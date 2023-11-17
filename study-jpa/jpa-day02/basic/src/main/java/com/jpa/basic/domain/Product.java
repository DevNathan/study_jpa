package com.jpa.basic.domain;

import com.jpa.basic.domain.type.ProductStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "JPA_PRODUCT")
public class Product {
    @Id @GeneratedValue
    private Long id;
    private String brand;
    private String name;
    private int price;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private LocalDate releaseDate;
}















