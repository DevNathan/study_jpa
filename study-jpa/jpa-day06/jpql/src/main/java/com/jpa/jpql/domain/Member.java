package com.jpa.jpql.domain;

import com.jpa.jpql.embedded.Address;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_MEMBER")
@Getter @Setter @ToString(exclude = "boards")
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    @Embedded // @Embeddable 클래스를 필드로 사용할 때 작성한다.
    private Address address;
    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();
}
