package com.jpa.data02.domain.entity;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
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
import java.util.Optional;

import static com.jpa.data02.domain.entity.QEmployee.employee;


/*
QueryDsl
JPQL을 생성해주는 기술(오픈소스 프레임워크)
문자열이 아닌 메소드 체이닝 방식으로 JPQL을 만들 수 있기 때문에 문법 오류를
컴파일 단계에서 확인할 수 있으며, 직관적인 동적쿼리를 작성할 수 있다는 장점이 있다.
QueryDSL은 실제 엔티티가 아닌 엔티티의 정보를 담은 Q타입 객체를 사용하며, Q타입 클래스는 설정된 특정 위치에 생성된다.

표준 기술이 아니지만 JPA를 사용한다면 필수적으로 가져가는 기술이다.
표준이 아니기 때문에 설정의 번거로움과 버전 변경시 새로운 설정 방법을 찾아야한다는 단점이 있다.
 */
@SpringBootTest
@Transactional
@Commit
public class DslBasicTest {
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
    @DisplayName("QueryDSL 설정 테스트")
    void loadTest() {
//        QueryDSL을 사용하여 jpql을 생성하기 위해서는 JPAQueryFactory 객체가 필요하며,
//        entityManager를 넘겨줘야한다.
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QEmployee qEmployee = employee;

        List<Employee> empList = queryFactory.select(qEmployee).from(qEmployee).fetch();

        empList.forEach(System.out::println);
    }

    //    =========================================================================
    @Test
    @DisplayName("Q타입 생성하기")
    void createQType() {
//        1. 별칭 지정하여 생성
//        QEmployee qEmployee1 = new QEmployee("e");
//        2. 기본 객체 사용
//        QEmployee qEmployee2 = QEmployee.employee;

//        일반적으로 기본객체를 사용한다.
//        기본객체는 static이므로 static import를 활용하면 편하게 사용 가능
        QEmployee qEmployee = employee;
    }

    @Test
    @DisplayName("기본 select")
    void select01() {
        Employee foundEmp = queryFactory
                .select(employee) // select에 Q객체를 사용하면 전체 필드 조회
                .from(employee) // 엔티티가 아닌 Q객체를 대상으로 한다.
                .where(employee.email.eq("test@naver.com"))
                .fetchOne(); // 단건 조회는 fetchOne() 사용

        System.out.println("foundEmp = " + foundEmp);

        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(employee.name.ne("김철수")) // ne -> !=
                .fetch();//여러건 조회는 fetch() 사용

    }

    @Test
    @DisplayName("여러 조건 활용")
    void select02() {
//        gt() : 초과
//        goe() : 이상
//        lt() : 미만
//        loe() : 이하
        List<Employee> empList = queryFactory.selectFrom(employee)
                .where(employee.salary.gt(10_000))
                .fetch();

//        in(a,b,c,....)
//        notIn(a,b,c,....)
//        between(a,b)
        queryFactory.selectFrom(employee)
                .where(employee.salary.between(1_000, 10_000))
                .fetch();

        List<Employee> empList2 = queryFactory.selectFrom(employee)
                .where(employee.id.in(1L, 2L, 3L, 4L))
                .fetch();

        empList2.forEach(System.out::println);

//        like('keyword'), notLike('keyword')
//        contains('keyword') : %keyword%
//        startWith('keyword') : keyword%
//        endWith('keyword') : %keyword
        queryFactory.selectFrom(employee)
                .where(employee.email.like("test_"))
                .fetch();
    }

    @Test
    @DisplayName("and, or 테스트")
    void andOrTest() {
        queryFactory.selectFrom(employee)
                .where( //where 내부에서 and/or 사용 가능
                        employee.name.eq("김철수").and(employee.salary.eq(10_000))
                ).fetch();

        queryFactory.selectFrom(employee)
                .where(
                        employee.hireDate.after(LocalDate.of(2000, 1, 1))
                                .or(employee.salary.isNotNull())
                ).fetch();

//        and를 다음과 같이도 사용 가능 ****
        queryFactory.selectFrom(employee)
                .where(
                        employee.name.contains("김"), //where 내부에서 , 를 사용하여 조건을 넣으면 and처리
                        employee.department.isNull()
                ).fetch();
    }

    //    =========================================================================
    @Test
    @DisplayName("정렬하기")
    void sorting() {
        queryFactory.selectFrom(employee)
                .where(employee.salary.notIn(10_000))
                .orderBy(employee.salary.desc(), employee.hireDate.asc()) // asc() 생략 불가능
                .fetch();

//        null 데이터 순서 정하기
        em.persist(
                Employee.builder().name("test")
                        .salary(10_000)
                        .build()
        );

        List<Employee> empList = queryFactory.selectFrom(employee)
                .orderBy(employee.email.desc().nullsLast())
                .fetch();
        System.out.println("empList = " + empList);
    }

    @Test
    @DisplayName("실행 메소드")
    void executeMethod() {
//      쿼리 dsl을 실행하는 메소드
//        fetchOne() : 단 건 조회, 결과가 없으면 null / 2건 이상 조회되면 예외 발생
//        fetch() : 여러 건 조회, 리스트를 반환하고 결과가 없으면 빈 리스트를 반환

//        fetchFirst() : 여러 건 조회되어도 하나의 결과만 반환
        Employee employee1 = queryFactory.selectFrom(employee)
                .fetchFirst();
        System.out.println("employee1 = " + employee1);

//        fetchCount() : 카운트 쿼리로 변경 (deprecated)
        long count = queryFactory.selectFrom(employee)
                .fetchCount();

//        fetchResults() : 페이징 처리 결과 반환, total쿼리도 같이 실행해준다. (deprecated)
        QueryResults<Employee> results = queryFactory.selectFrom(employee)
                .orderBy(employee.id.desc())
                .offset(0) // 시작 행 설정 (0부터 시작)
                .limit(2) // 몇 건 조회인지 설정
                .fetchResults();

        System.out.println("total count : " + results.getTotal());
        System.out.println("limit : " + results.getLimit());
        System.out.println("offset : " + results.getOffset());
        System.out.println("result : " + results.getResults());
    }

    @Test
    @DisplayName("반환 타입")
    void returnType() {
//        엔티티 타입 : select()에 Q타입을 사용하면 자동으로 해당 엔티티 타입이 반환된다.
//        기본 타입 : 프로젝션으로 단일 필드를 지정하면 해당 타입으로 반환된다.
        List<String> names = queryFactory.select(employee.name)
                .from(employee)
                .fetch();
//        Tuple 타입 : 프로젝션으로 여러 필드를 지정하면 Tuple타입으로 반환된다.
//                    조회 결과를 엔티티 / 기본 타입으로 받을 수 없는 경우 Tuple이 반환된다.
        List<Tuple> tupleList = queryFactory.select(employee.name, employee.email)
                .from(employee)
                .fetch();

//        Tuple타입은 쿼리 dsl에서 제공하는 타입이며 다음과 같이 사용한다.
//        빠른 for문 라이브 템플릿 iter
        for (Tuple tuple : tupleList) {
//            get(조회할 때 사용한 프로젝션)
            String name = tuple.get(employee.name);
            String email = tuple.get(employee.email);
            System.out.println(name + " : " + email);
        }
    }

    @Test
    @DisplayName("group by와 집계함수")
    void groupBy() {
//       count() , sum() , avg() , min(), max() 모두 지원
        Long count = queryFactory.select(employee.count())
                .from(employee)
                .fetchOne();
        System.out.println("count = " + count);

        Double avg = queryFactory.select(employee.salary.avg())
                .from(employee)
                .fetchOne();
        System.out.println("avg = " + avg);

        StringPath projectionName = employee.department.name;
        NumberExpression<Double> projectionAvg = employee.salary.avg();
//        group by와 having을 지원함
        List<Tuple> list = queryFactory.select(projectionName, projectionAvg)
                .from(employee)
                .groupBy(employee.department.name)
                .having(employee.department.name.ne("test"))
                .fetch();

        System.out.println("list = " + list);

        for (Tuple tuple : list) {
            String name = tuple.get(projectionName);
            Double salaryAvg = tuple.get(projectionAvg);
            System.out.println(name + " : " + salaryAvg);
        }
    }

    //    ==================================================================================
//    실습
//    1. 모든 사원들의 이름, 소속부서 이름을 조회, 출력하기
    @Test
    @DisplayName("실습1")
    void task01() {
        List<Tuple> tupleList = queryFactory
                .select(employee.name, employee.department.name)
                .from(employee)
                .fetch();

        for (Tuple tuple : tupleList) {
            System.out.println("empName : " + tuple.get(employee.name));
            System.out.println("deptName : " + tuple.get(employee.department.name));
        }

    }

    //    2. 급여가 10,000이상인 사원들의 이름과 급여 조회, 출력하기
    @Test
    @DisplayName("실습2")
    void task02() {
        List<Tuple> tupleList = queryFactory
                .select(employee.name, employee.salary)
                .from(employee)
                .where(employee.salary.goe(10_000))
                .fetch();

        tupleList.forEach(tuple -> {
            System.out.println("name : " + tuple.get(employee.name));
            System.out.println("salary : " + tuple.get(employee.salary));
        });
    }

    //    3. 가장 오래전에 입사한 사원 조회, 출력하기
//        가장 최근에 입사한 사원 조회, 출력하기
    @Test
    @DisplayName("실습3")
    void task03() {
        Employee foundEmp = queryFactory.selectFrom(QEmployee.employee)
                .orderBy(QEmployee.employee.hireDate.asc())
                .fetchFirst();
        System.out.println("employee = " + foundEmp);

        Optional<Employee> foundEmp2 = Optional.ofNullable(
                queryFactory.selectFrom(QEmployee.employee)
                        .orderBy(QEmployee.employee.hireDate.desc())
                        .fetchFirst()
        );
        System.out.println("employee = " + foundEmp2.get());
    }
//    4. 각 부서별 직원 수를 조회, 출력하기
    @Test
    @DisplayName("실습4")
    void task04(){
        List<Tuple> tupleList = queryFactory
                .select(employee.department.name, employee.count())
                .from(employee)
                .groupBy(employee.department.name)
                .fetch();

        for (Tuple tuple : tupleList) {
            System.out.println("deptName : " + tuple.get(employee.department.name));
            System.out.println("count : " + tuple.get(employee.count()));
        }
    }

}














