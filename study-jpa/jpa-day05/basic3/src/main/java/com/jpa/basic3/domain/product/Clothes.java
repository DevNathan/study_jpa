package com.jpa.basic3.domain.product;

import com.jpa.basic3.domain.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "JPA_CLOTHES")
@DiscriminatorValue("C")
@PrimaryKeyJoinColumn(name = "CLOTHES_ID")
@Getter @Setter
public class Clothes extends Product {
    @Column(name = "CLOTHES_SIZE")
    private int size;
    private String color;
}
