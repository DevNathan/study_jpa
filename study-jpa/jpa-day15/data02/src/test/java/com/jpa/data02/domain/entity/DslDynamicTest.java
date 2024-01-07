package com.jpa.data02.domain.entity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
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
public class DslDynamicTest {
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
                .phone("02-2222-1238")
                .officeLocation("B")
                .build();

        em.persist(dept1);
        em.persist(dept2);

        Employee employee1 = Employee.builder()
                .name("김철수")
                .salary(10_000)
                .hireDate(LocalDate.of(2000, 10, 10))
                .email("test@gmail.com")
                .department(dept1)
                .build();

        Employee employee2 = Employee.builder()
                .name("홍길동")
                .salary(20_000)
                .hireDate(LocalDate.of(2000, 1, 1))
                .email("hong@gmail.com")
                .department(dept1)
                .build();

        Employee employee3 = Employee.builder()
                .name("신사임당")
                .salary(18_000)
                .hireDate(LocalDate.of(2012, 12, 1))
                .email("shin@gmail.com")
                .department(dept2)
                .build();

        em.persist(employee1);
        em.persist(employee2);
        em.persist(employee3);
    }

    @Test
    @DisplayName("jpql 동적 쿼리")
    void jpqlDynamicQuery() {
//        jpql을 직접 사용하는 경우 동적쿼리

//        아래 변수는 매개변수로 받았다고 가정한다.
        String searchType = "name";
        String keyword = "김철수";

        String jpql = "select e from Employee e ";

        if (searchType.equals("name")) {
            jpql += "where e.name = :keyword";
        } else if (searchType.equals("salary")) {
            jpql += "where e.salary = :keyword";
        }

        List<Employee> empList = em.createQuery(jpql, Employee.class)
                .setParameter("keyword", keyword)
                .getResultList();

        System.out.println("empList = " + empList);

//        이런 동적 쿼리 생성은 문자열을 기반으로 만들어지기 때문에 오류를 찾기 힘들고 가독성이 떨어진다.
    }

    @Test
    @DisplayName("QueryDsl 동적쿼리01")
    void dslDynamicQuery() {
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

//        queryDSL로 동적 쿼리를 만들 때 2가지 방법을 사용한다.
//        1. BooleanBuilder를 사용한다.
//        BooleanBuilder는 where절에 사용할 조건을 동적으로 만들어줄 수 있다.
//        and(), or()를 사용하여 쿼리를 생성한다.

        BooleanBuilder builder = new BooleanBuilder();

        QEmployee employee = QEmployee.employee;

        if ("name".equals(searchType)) {
            builder.and(employee.name.eq(keyword));
        } else if ("salary".equals(searchType)) {
            builder.and(employee.salary.eq(Integer.parseInt(keyword)));
        }

        if (date != null) {
            builder.and(employee.hireDate.after(date));
        }

        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(builder)
                .fetch();

        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("QueryDsl 동적쿼리02")
    void dslDynamicQuery02() {
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

//        queryDSL로 동적 쿼리를 만들 때 2가지 방법을 사용한다.
//        1. BooleanBuilder를 사용한다.
//        BooleanBuilder는 where절에 사용할 조건을 동적으로 만들어줄 수 있다.
//        and(), or()를 사용하여 쿼리를 생성한다.
        QEmployee employee = QEmployee.employee;

//        동적 쿼리 조건 생성 코드가 복잡하거나 반복적으로 여러 코드에서 사용된다면 메소드로 분리하여 가독성과 유지보수성을 높일 수 있다.
        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(searchCond1(null, null, null))
                .fetch();

        System.out.println("empList = " + empList);
    }

    BooleanBuilder searchCond1(String searchType, String keyword, LocalDate date) {
        BooleanBuilder builder = new BooleanBuilder();

        QEmployee employee = QEmployee.employee;

        if (searchType.equals("name")) {
            builder.and(employee.name.eq(keyword));
        } else if ("salary".equals(searchType)) {
            builder.and(employee.salary.eq(Integer.parseInt(keyword)));
        }

        if (date != null) {
            builder.and(employee.hireDate.after(date));
        }

        return builder;
    }

    //    실습
//    name과 officeLocation을 입력받아 부서를 조회한다.
//    name또는 officeLocation이 일치하는 부서를 조회한다.
//    둘 중 하나라도 조건이 null일시 조건을 생략한다.

    @Test
    @DisplayName("동적 쿼리 실습01")
    void dynamicQueryTask01() {
        List<Department> deptList = queryFactory.selectFrom(department)
                .where(departmentSearchCond("개발", null))
                .fetch();

        System.out.println("deptList = " + deptList);
    }

    BooleanBuilder departmentSearchCond(String name, String officeLocation) {
        BooleanBuilder builder = new BooleanBuilder();

        if (name == null || officeLocation == null) {
            return builder;
        }

        return builder.or(department.name.eq(name))
                .or(department.officeLocation.eq(officeLocation));
    }

    @Test
    @DisplayName("QueryDsl 동적쿼리03")
    void dslDynamicQuery03() {
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

//        2. where()에서 , 를 사용하면 여러 조건을 and로 연결해준다.
//        where(조건1, 조건2) -> 조건1 and 조건2
//        where에 넘긴 조건이 nulll인 경우 null을 무시한다는 특징이 있다.
//

        queryFactory.selectFrom(employee)
                .where(searchCond2(searchType, null),
                        date == null ? null : employee.hireDate.after(date))
                .fetch();
    }

    Predicate searchCond2(String searchType, String keyword) {
        if (searchType == null || keyword == null) {
            return null;
        }

        if (searchType.equals("name")) {
            return employee.name.eq(keyword);
        }

        return employee.salary.eq(Integer.parseInt(keyword));

    }

//    실습
//    사원의 이름, 부서 이름, 부서 전화번호를 전달받아 모두 일치하는 사원이름, 해당 사원의 소속된 부서 정보를 조회
//    모든 조건은 부분일치 조건으로 사용한다.
//    부서 전화번호 : 2 -> 2를 포함하는 조건
//    만약 null인 경우 해당조건은 생략
//    BooleanBuilder를 사용하지 않는다.

    Predicate searchEmployees(String searchType, String keyword) {
        if (searchType == null || keyword == null) return null;

        if ("name".equals(searchType)) {
            return employee.name.eq(keyword);
        } else if ("departmentName".equals(searchType)) {
            return employee.department.name.eq(keyword);
        } else {
            return employee.department.phone.like(keyword);
        }
    }

    @Test
    @DisplayName("task2")
    void task2(){
        String searchType = "name";
        String keyword = "김철수";
        LocalDate date = LocalDate.of(2000, 1, 2);

        List<Employee> list1 = queryFactory.selectFrom(employee)
                .where(searchEmployees(searchType, keyword),
                        date == null ? null : employee.hireDate.after(date))
                .fetch();

        System.out.println("list1 = " + list1);

        searchType = "departmentName";
        keyword = "디자인";

        List<Employee> list2 = queryFactory.selectFrom(employee)
                .where(searchEmployees(searchType, keyword),
                        date == null ? null : employee.hireDate.after(date))
                .fetch();

        System.out.println("list2 = " + list2);

        searchType = "몰라";
        keyword = "2";

        List<Employee> list3 = queryFactory.selectFrom(employee)
                .where(searchEmployees(keyword, keyword),
                        date == null ? null : employee.hireDate.after(date))
                .fetch();

        System.out.println("list3 = " + list3);
    }
}