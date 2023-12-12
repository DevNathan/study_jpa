package com.jpa.data01.domain.entity;

import com.jpa.data01.domain.base.Period;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "JPA_CHECK_OUT")
@SequenceGenerator(
        name = "SEQ_CHECK_OUT_GENERATOR",
        sequenceName = "SEQ_JPA_CHECK_OUT"
)
@Getter @Setter @ToString
@SQLDelete(sql = "UPDATE JPA_CHECK_OUT SET DELETED = TRUE WHERE CHECK_OUT_ID = ?")
@Where(clause = "DELETED = false")
public class CheckOut extends Period {
    @Id @GeneratedValue(generator = "SEQ_CHECK_OUT_GENERATOR")
    @Column(name = "CHECK_OUT_ID")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOOK_ID")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_ID")
    private User user;
    private boolean deleted = false;

//    편의 메소드
    public void setUser(User user){
        if(this.user != null){
            this.user.getCheckOutList().remove(this);
        }
        this.user = user;
        user.getCheckOutList().add(this);
    }

    public void setBook(Book book){
        this.book = book;
        book.setCheckOut(this);
    }
}








