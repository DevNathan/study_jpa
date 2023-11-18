package com.jpa.basic2.domain;

import com.jpa.basic2.domain.id.OrderId;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "JPA_ORDER")
@IdClass(OrderId.class)
@Data
public class Order {
    @Id
    @ManyToOne @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @Id
    @ManyToOne @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    private int amount;
}
