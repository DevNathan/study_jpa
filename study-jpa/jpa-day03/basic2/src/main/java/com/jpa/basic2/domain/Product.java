package com.jpa.basic2.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_PRODUCT")
@SequenceGenerator(
        name = "SEQ_PRODUCT_GENERATOR",
        sequenceName = "SEQ_JPA_PRODUCT"
)
@Getter
@Setter
//@ToString(exclude = "memberList")
public class Product {
    @Id
    @GeneratedValue(generator = "SEQ_PRODUCT_GENERATOR")
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;
    private int price;
    //    @ManyToMany(mappedBy = "productList")
//    private List<Member> memberList = new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();
}
