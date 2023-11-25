package com.jpa.basic3.domain.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jpa.basic3.domain.base.Period;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_USER")
@SequenceGenerator(
        name = "SEQ_USER_GENERATOR",
        sequenceName = "SEQ_JPA_USER"
)
@Getter @Setter @ToString(exclude = "boardList")
public class User extends Period implements Serializable {
    @Id @GeneratedValue(generator = "SEQ_USER_GENERATOR")
    private Long id;
    private String userName;
    private String loginId;
    private String password;
    private int age;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();
}
