package com.jpa.data01.domain.entity;

import com.jpa.data01.domain.base.Period;
import com.jpa.data01.domain.embedded.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JPA_USER")
@SequenceGenerator(
        name = "SEQ_USER_GENERATOR",
        sequenceName = "SEQ_JPA_USER"
)
@SQLDelete(sql = "UPDATE JPA_USER SET DELETED = TRUE WHERE USER_ID = ?")
@Where(clause = "DELETED = false")
@Getter @Setter @ToString(exclude = "checkOutList")
public class User extends Period {
    @Id @GeneratedValue(generator = "SEQ_USER_GENERATOR")
    @Column(name = "USER_ID")
    private Long id;
    private String name;
    private String phone;
    private LocalDate birth;
    @Embedded
    private Address address;
    private boolean deleted = false;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<CheckOut> checkOutList = new ArrayList<>();
}








