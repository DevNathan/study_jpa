package com.jpa.data01.domain.entity;

import com.jpa.data01.domain.base.Period;
import com.jpa.data01.domain.type.BookCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "JPA_BOOK")
@Getter @Setter @ToString(exclude = "checkOut")
@SQLDelete(sql = "UPDATE JPA_BOOK SET DELETED = TRUE WHERE BOOK_ID = ?")    //delete를 하는 상황에서 대신 날릴 쿼리를 작성
//@Where(clause = "DELETED = false")
@SQLRestriction("DELETED = FALSE") // 조회할 때 항상 추가되는 where절을 설정
@SequenceGenerator(
        name = "SEQ_BOOK_GENERATOR",
        sequenceName = "SEQ_BOOK",
        allocationSize = 1
)
public class Book extends Period {
    @Id @GeneratedValue(generator = "SEQ_BOOK_GENERATOR")
    @Column(name = "BOOK_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private BookCategory category;
    private String name;
    private Integer price;
    private LocalDate releaseDate;
    private boolean deleted = false;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "book")
    private CheckOut checkOut;

}










