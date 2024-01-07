package com.jpa.data02.domain.entity;

import com.jpa.data02.domain.dto.DepartmentDto;
import com.jpa.data02.domain.dto.EmployeeDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
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
    @DisplayName("문자열이 아닌 값과 연결하기")
    void concat2(){
        List<String> list = queryFactory.select(employee.name.concat(" : ").concat(employee.salary.stringValue()))
                .from(employee)
                .fetch();

        System.out.println("list = " + list);
    }
    
    @Test
    @DisplayName("case문")
    void caseWhenThen(){
        List<String> list = queryFactory.select(employee.department.name
                        .when("개발").then("야근").when("디자인").then("퇴근").otherwise("퇴사")
                ) // 이 방식을 사용하면 when에는 = 조건만 사용 가능하다. otherwise()는 필수이다.
                .from(employee)
                .fetch();

        System.out.println("list = " + list);
    }

    @Test
    @DisplayName("case문2")
    void caseWhenThen2(){
//        같다(=) 조건 외에 여러 조건을 사용하고 싶으면 CaseBuilder를 활용한다.
        StringExpression salaryGrade = new CaseBuilder().when(employee.salary.gt(20_000)).then("부자")
                .when(employee.salary.gt(15_000)).then("평균")
                .when(employee.salary.gt(10_000)).then("거지")
                .otherwise("몰?루");

        List<String> list = queryFactory.select(salaryGrade).from(employee).fetch();

        System.out.println("list = " + list);
    }

    @Test
    @DisplayName("상수 조회하기")
    void constantSelect(){
        List<Tuple> tupleList = queryFactory.select(employee, Expressions.constant(100)).from(employee).fetch();

        tupleList.forEach(tuple -> {
            System.out.println("emp : " + tuple.get(employee));
            System.out.println("constant " + tuple.get(Expressions.constant(100)));
        });
    }

    @Test
    @DisplayName("Dto 반환 받기")
    void returnDto(){
//        jpql
//        em.createQuery("select new com.jpa.data02.domain.dto.EmployeeDto(e.name, e.salary, e.hireDate) " +
//                "from Employee e", Employee.class).getResultList();

//        queryDSL에서는 프로젝션을 Dto로 반환받기 위해 Projections 타입을 사용한다.
//        Projections로 Dto를 반환받는 방법은 3가지 방법이 있다.

//        1. fields() : 필드 직접 접근 방식
//        - 필드에 직접 접근하므로 프로젝션의 순서 갯수와 상관없이 반환받을 수 있다.
//        - 생성자나 setter 없이도 Dto를 반환 받을 수 있다.
//        - 필드가 private이어도 상관이 없다(리플렉션을 사용하기 때문에).
//        단, QueryDSL이 Dto 객체를 생성할 때 기본생성자를 사용하므로 Dto에 기본생성자는 필수이다.
        List<EmployeeDto> list = queryFactory.select(
                    Projections.fields(EmployeeDto.class, employee.name, employee.hireDate)
                )
                .from(employee)
                .fetch();

        System.out.println("list = " + list);
        
//        2. bean() : setter를 통한 접근
//        - setter를 사용하므로 프로젝션의 순서, 갯수와 상관없이 프로젝션을 선택할 수 있다.
//        - 기본 생성자가 필수이다.
        List<EmployeeDto> list2 = queryFactory.select(
                Projections.bean(EmployeeDto.class, employee.name, employee.salary)
        ).from(employee).fetch();

        System.out.println("list2 = " + list2);

//        3. constructor() : 생성자로 초기화
//        - 생성자를 사용하므로 dto에 만들어놓은 생성자의 파라미터 순서와 갯수에 맞게 전달해야한다.
//        기본 생성자가 필요없다. (근데 어차피 dto를 빈등록해서 사용하려면 기본생성자를 만든다.)
        List<EmployeeDto> list3 = queryFactory.select(
                        Projections.constructor(EmployeeDto.class, employee.name, employee.salary, employee.hireDate)
                )
                .from(employee)
                .fetch();

        System.out.println("list3 = " + list3);
    }

    @Test
    @DisplayName("Dto 리턴 받기")
    void returnDto2(){
//        만약 엔티티의 필드명과 Dto의 필드명이 다르다면?
//        List<DepartmentDto> deptList = queryFactory.select(
//                        Projections.bean(DepartmentDto.class, department.name, department.phone, department.officeLocation)
//                )
//                .from(department)
//                .fetch();
//
//        System.out.println("deptList = " + deptList);
//        필드명과 일치하지 않아 데이터가 바인딩되지 않는다.

//        이 경우 마이바티스와 동일하게 별칭을 활용하면 된다.
        List<DepartmentDto> deptList = queryFactory.select(
                        Projections.bean(DepartmentDto.class, department.name,
                                department.phone.as("phoneNumber"), department.officeLocation.as("office"))
                )
                .from(department)
                .fetch();

        System.out.println("deptList = " + deptList);
    }
}
