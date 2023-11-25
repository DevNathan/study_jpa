package com.jpa.basic3.domain.task;

import com.jpa.basic3.domain.base.Period;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "JPA_BOARD")
@SequenceGenerator(
        name = "SEQ_BOARD_GENERATOR",
        sequenceName = "SEQ_JPA_BOARD"
)
@Getter @Setter @ToString
public class Board extends Period {
    @Id @GeneratedValue(generator = "SEQ_BOARD_GENERATOR")
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;
}
