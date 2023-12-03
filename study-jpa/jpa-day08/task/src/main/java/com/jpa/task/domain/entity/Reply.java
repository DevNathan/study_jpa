package com.jpa.task.domain.entity;

import com.jpa.task.domain.base.Period;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "JPA_REPLY")
@Getter @Setter @ToString
@NoArgsConstructor
@SequenceGenerator(
        name = "SEQ_REPLY_GENERATOR",
        sequenceName = "SEQ_REPLY"
)
public class Reply extends Period {
    @Id @GeneratedValue(generator = "SEQ_REPLY_GENERATOR")
    @Column(name = "REPLY_ID")
    private Long id;
    @Column(length = 1000)
    private String content;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
