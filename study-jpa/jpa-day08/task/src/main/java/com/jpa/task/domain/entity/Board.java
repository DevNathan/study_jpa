package com.jpa.task.domain.entity;

import com.jpa.task.domain.base.Period;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "JPA_BOARD")
@Getter @Setter @ToString
@NoArgsConstructor
@SequenceGenerator(
        name = "SEQ_BOARD_GENERATOR",
        sequenceName = "SEQ_BOARD"
)
public class Board extends Period {
    @Id @GeneratedValue(generator = "SEQ_BOARD_GENERATOR")
    @Column(name = "BOARD_ID")
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;
    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
