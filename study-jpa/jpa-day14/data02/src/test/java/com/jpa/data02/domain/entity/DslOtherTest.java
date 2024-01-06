package com.jpa.data02.domain.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.jpa.data02.domain.entity.QEmployee.employee;

@SpringBootTest
@Transactional @Commit
public class DslOtherTest {
    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);

        Department dept1 = Department.builder()
                .name("개발")
                .phone("02-1111-1111")
                .officeLocation("A")
                .build();

        Department dept2 = Department.builder()
                .name("디자인")
                .phone("02-2222-2222")
                .officeLocation("B")
                .build();

        em.persist(dept1);
        em.persist(dept2);

        Employee employee1 = Employee.builder()
                .name("김철수")
                .salary(10_000)
                .hireDate(LocalDate.of(2000, 10, 10))
                .email("test@naver.com")
                .department(dept1)
                .build();

        Employee employee2 = Employee.builder()
                .name("홍길동")
                .salary(20_000)
                .hireDate(LocalDate.of(2010, 1, 1))
                .email("hong@naver.com")
                .department(dept1)
                .build();

        Employee employee3 = Employee.builder()
                .name("이지웅")
                .salary(18_000)
                .hireDate(LocalDate.of(2020, 7, 22))
                .email("woong@naver.com")
                .department(dept2)
                .build();

        em.persist(employee1);
        em.persist(employee2);
        em.persist(employee3);
    }

    @Test
    @DisplayName("조회 결과 연결하기")
    void concat(){
        List<String> concatList = queryFactory
                .select(employee.name.concat(" : ").concat(employee.email))
                .from(employee)
                .fetch();

        System.out.println("concatList = " + concatList);
    }

    @Test
    @DisplayName("문자열이 아닌 값과 연결하기")
    void concat2(){
//        sql 또는 Jpql에서는 concat에 문자열이 아닌 값을 넣어도 자동 형변환된다.
//        그러나 queryDSL은 concat의 매개변수 타입이 String 이므로 문자열을 넣어야한다.
//        이런 경우 해당 필드에 stringValue()를 사용하여 문자열 값으로 변경해주면 된다.
        List<String> concatList = queryFactory
                .select(employee.name.concat(" : ").concat(employee.salary.stringValue()))
                .from(employee)
                .fetch();
        System.out.println("concatList = " + concatList);
    }
}









