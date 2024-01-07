package com.jpa.data02.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "JPA_EMPLOYEE")
@Getter @ToString
// 엔티티는 기본생성자가 필수이다.
// Builder패턴을 적용하기 위해 추가 생성자를 만들어야 하므로
// NoArgsConstructor를 명시해야 한다.
// 그런데 기본생성자가 존재한다면 불필요한 엔티티가 생성될 가능성이 열린다.
// 그러므로 접근제한자를 Protected로 변경하여 외부에서는 접근을 못하게 막아준다.
// JPA는 protected 레벨의 생성자까지는 사용이 가능하다.
// (프록시 객체를 만들 때 상속을 사용하므로 가능)
@SequenceGenerator(
        name = "SEQ_EMPLOYEE_GENERATOR",
        sequenceName = "SEQ_EMPLOYEE",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {
    @Id @GeneratedValue(generator = "SEQ_EMPLOYEE_GENERATOR")
    @Column(name = "EMPLOYEE_ID")
    private Long id;
    private String name;
    private int salary;
    private String email;
    private LocalDate hireDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Builder
    public Employee(Long id, String name, int salary, String email, LocalDate hireDate, Department department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.email = email;
        this.hireDate = hireDate;
        this.department = department;
    }

    public void modifyEmail(String email){

        this.email = email;
    }
}
