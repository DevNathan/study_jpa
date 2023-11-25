package com.jpa.basic3.domain2;

import com.jpa.basic3.embedded.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "JPA_MEMBER")
@Data
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    @Embedded // @Embeddable 클래스를 필드로 사용할 때 작성한다.
    private Address address;
}
