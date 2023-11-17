package com.jpa.basic.domain;

import com.jpa.basic.domain.type.MemberGender;
import lombok.Data;

import javax.persistence.*;

@Entity // Entity로 등록한다. 테이블 이름은 Entity의 이름과 동일하게 만들어진다.
// 엔티티를 만드는 순간 오류가 나는 이유는 테이블을 만들 때 반드시 필요한 PK가 없기 때문
// 엔티티는 기본생성자 필수!
@Table(name = "JPA_MEMBER") // 테이블 이름을 별도로 설정해줄 수 있다.
@Data
public class Member {
    @Id // 해당 필드를 pk칼럼으로 등록한다.
//   GeneratedValue를 사용하면 해당 컬럼에 자동 증가되는 값이 들어가게된다.
//    AUTO : 자동으로 하이버네이트가 해당 DBMS에 맞게 설정해주며 이 값이 디폴트
//    IDENTITY : AUTO_INCREMENT를 사용하는 MySQL, IDENTITY를 사용하는 MS SQL의 기본키 생성 전략
//    SEQUENCE : 오라클, PostgreSQL등 시퀀스 객체를 사용하는 기본키 생성 전략
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private int age;
    @Enumerated(EnumType.STRING)
    private MemberGender gender;
}











