package com.jpa.jpql.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "JPA_BOARD")
@Getter @Setter @ToString
public class Board {
    @Id @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
