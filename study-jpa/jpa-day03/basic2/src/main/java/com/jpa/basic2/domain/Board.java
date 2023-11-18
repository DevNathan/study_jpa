package com.jpa.basic2.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "JPA_BOARD")
@Data
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long Id;
    private String title;
    private String content;
    @ManyToOne // 다대일 연관관계 매핑
    @JoinColumn(name = "USER_ID") // 외래키로 사용할 컬럼명을 매핑한다. (이렇게 하지 않아도 USER테이블의 ID를 붙혀나오기때문에 이미 USER_ID로 나옴)
    private User user;
}
