package com.jpa.basic2.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_USER")
@Getter
@Setter
@ToString(exclude = "boardList")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;
    private String userName;
    private String loginId;
    private String password;
    private int age;
//    @OneToMany
//    @JoinColumn(name = "USER_ID") // 일대다관계에서는 필수로 작성, 양방향관계에서는 불필요.
//    양방향 매핑을 하는 경우
//    연관관계의 주인이 아닌쪽에 mappedBy를 사용한다.
//    반대쪽 엔티티의 필드 이름을 작성해준다.
    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>(); //NPE 방어 차원에서 new
}
