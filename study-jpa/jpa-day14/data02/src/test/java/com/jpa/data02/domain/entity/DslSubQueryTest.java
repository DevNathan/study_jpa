package com.jpa.data02.domain.entity;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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

import static com.jpa.data02.domain.entity.QDepartment.department;
import static com.jpa.data02.domain.entity.QEmployee.employee;

@SpringBootTest
@Transactional
@Commit
public class DslSubQueryTest {
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
    @DisplayName("서브 쿼리 : where 1")
    void subQuery01() {
//        급여가 가장 높은 사원 조회
//        select e from Employee e where salary = (select max(e2.salary) from Employee e2)

        QEmployee qEmployee = employee; // 기본 제공 Q타입의 별칭은 이름과 동일한 employee이다.
        QEmployee subEmployee = new QEmployee("e2");//우리가 직접 별칭을 지정하여 만든 e2이다.

//        QueryDSL에서 서브쿼리를 사용하는 경우 JPAExpressions 타입을 사용한다.
        List<Employee> empList = queryFactory.selectFrom(qEmployee)
                .where(qEmployee.salary.eq(
                        JPAExpressions.select(subEmployee.salary.max())
                                .from(subEmployee)
                )).fetch();
        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("서브 쿼리 : where 2")
    void subQuery02() {
//      부서 별 급여가 가장 높은 사원 조회 (조건에 In을 사용하기)
        QEmployee subEmp = new QEmployee("subEmp");

        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(employee.salary.in(
                        JPAExpressions.select(subEmp.salary.max())
                                .from(subEmp)
                                .join(subEmp.department, department)
                                .groupBy(department.name)
                )).fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("서브 쿼리 : select 1")
    void scalar01() {
        QEmployee subEmp = new QEmployee("subEmp");

        List<Tuple> tupleList = queryFactory.select(
                employee.name,
                JPAExpressions.select(subEmp.salary.avg()).from(subEmp)
        )
                .from(employee)
                .fetch();

        tupleList.forEach(tuple -> {
            System.out.println("name : " + tuple.get(employee.name));
            System.out.println("salary avg : " + tuple.get(
                    JPAExpressions.select(subEmp.salary.avg()).from(subEmp)
            ));
        });
    }

    @Test
    @DisplayName("서브 쿼리 : select 2")
    void scalar02() {
//        서브 쿼리를 다음과 같이 분리할 수 있다.(가독성)
        JPQLQuery<String> subQuery = JPAExpressions.select(department.officeLocation)
                .from(department)
                .where(department.id.eq(employee.department.id));

        List<Tuple> tupleList = queryFactory.select(employee.name, subQuery)
                .from(employee)
                .fetch();

        tupleList.forEach(tuple -> {
            System.out.println("name : " + tuple.get(employee.name));
            System.out.println("office location : " + tuple.get(subQuery));
        });
    }

    @Test
    @DisplayName("서브 쿼리 : from 1")
    void inlineView01(){
//        하이버 네이트 6.1버전 부터 from절 서브쿼리 사용이 가능하다.
//        QueryDSL 개발자 : from절 서브쿼리가 어느정도 안정된 6.x버전이 배포되면 개발해줌ㅋ
//        그러므로 from절 서브쿼리를 쓰고 싶으면 entityManager를 이용하여 jpql을 작성해야한다.
    }
}













