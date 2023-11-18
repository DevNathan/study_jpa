package com.jpa.basic2.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_MEMBER")
@Data
@SequenceGenerator(
        name = "SEQ_MEMBER_GENERATOR",
        sequenceName = "SEQ_JPA_MEMBER"
)
public class Member {
    @Id
    @GeneratedValue(generator = "SEQ_MEMBER_GENERATOR")
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    private String address;
//    @ManyToMany
//    @JoinTable(
//            name = "JPA_ORDER",
//            joinColumns = @JoinColumn(name = "MEMBER_ID"),
//            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")// 회원과 매핑할 컬럼 지정
//    ) // 이렇게 설정하면 중간테이블인 ORDER 엔티티 없이 연결이 가능하다.
//    private List<Product> productList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
