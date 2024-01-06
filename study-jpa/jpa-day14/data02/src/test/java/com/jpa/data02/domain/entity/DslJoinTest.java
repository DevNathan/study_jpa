package com.jpa.data02.domain.entity;

import com.querydsl.core.Tuple;
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
@Transactional @Commit
public class DslJoinTest {
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
    @DisplayName("기본 join")
    void basicJoin(){
        QEmployee qEmployee = employee;
        QDepartment qDepartment = department;

        List<Employee> empList1 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department)
                .fetch();

//        Join 후 department를 이용하기가 불편하다.
        List<Employee> empList2 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department)
                .where(qEmployee.department.name.ne("test"))
                .fetch();

//        qEmployee.department 대신 별칭을 지정할 수 있다.
        List<Employee> empList3 = queryFactory.selectFrom(qEmployee)
                .join(qEmployee.department, qDepartment) //join(조인할 대상, 별칭) -> 별칭은 q타입을 사용
                .where(qDepartment.name.ne("test"))
                .fetch();
    }

    @Test
    @DisplayName("where절에 의한 join")
    void whereJoin(){
//       where의 조건에 연관관계를 맺은 엔티티를 사용하면 자동 join을 사용한다.(pk=fk 조건)
        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(employee.department.officeLocation.eq("A"))
                .fetch();
    }

    @Test
    @DisplayName("Outer Join")
    void outerJoin(){
        em.flush();
        em.clear();

        List<Employee> empList = queryFactory.selectFrom(employee)
                .leftJoin(employee.department, department)
                .where(department.name.eq("개발"))
                .fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("카르테시안 곱")
    void crossJoin(){
//        주로 연관관계가 없는 필드로 조인을 걸고 싶거나 아예 무관한 엔티티끼리 조인을 걸고 싶을때 사용했었다.
        List<Tuple> tupleList = queryFactory.select(employee, department)
                .from(employee, department)
                .fetch();

        tupleList.forEach(System.out::println);

//        카르테시안 곱으로 조인하는 경우 where의 조건을 활용하여 원하는 데이터를 골라내야한다.
        em.persist(
                Employee.builder().email("A").build()
        );

        List<Tuple> tupleList1 = queryFactory.select(employee, department)
                .from(employee, department)
                .where(employee.email.eq(department.officeLocation))
                .fetch();
        tupleList1.forEach(System.out::println);

//        예전 버전에서는 연관관계가 없는 조인이 불가능하여 어쩔수 없이 사용하는 경우가 있었으나
//        지금은 하이버 네이트 버전이 올라가면서 연관관계가 없는 join on 조건이 사용 가능해졌다.
    }
    
    @Test
    @DisplayName("on절이 없는 join")
    void crossJoin2(){
//        join을 명시하여 사용하는 경우 on절이 생략되면 카르테시안 곱이 발생될 수 있다.
        List<Tuple> tupleList = queryFactory
                .select(employee, department)
                .from(employee)
                .leftJoin(department)
                .fetch();

        tupleList.forEach(System.out::println);
    }

    @Test
    @DisplayName("on절 사용하여 연관관계가 없는 조건 넣기")
    void joinOn(){
        em.persist(
                Employee.builder().email("A").build()
        );
        em.flush();
        em.clear();
//        select에 정확히 조회대상을 명시하여 employee, department 엔티티 둘 다 가져오는 경우( 둘 다 영속화)
//        List<Tuple> tupleList = queryFactory.select(employee, department)
//                .from(employee)
//                .join(department).on(employee.email.eq(department.officeLocation))
//                .fetch();
//
//        tupleList.forEach(System.out::println);
//        em.find(Employee.class, 4L); // 영속 상태이므로 select쿼리 안나감
//        em.find(Department.class, 1L); // 영속 상태이므로 select쿼리 안나감

//        조인을 사용하였으나 department를 조건에서만 사용하고 조회는 하지 않은경우 (Employee만 영속화)
        List<Employee> empList = queryFactory.select(employee)
                .from(employee)
                .join(department).on(employee.email.eq(department.officeLocation))
                .fetch();

        em.find(Department.class, 1L);
    }

    @Test
    @DisplayName("fetch join")
    void fetchJoin(){
        em.flush();
        em.clear();

        List<Employee> empList = queryFactory.selectFrom(employee)
                .join(employee.department, department).fetchJoin()
                .fetch();

        empList.forEach(System.out::println);
    }

}










