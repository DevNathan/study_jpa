package com.jpa.data02.repository;

import com.jpa.data02.domain.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class EmployeeJpaRepositoryTest {

    @Autowired
    EmployeeJpaRepository employeeJpaRepository;

    Employee emp;

    @BeforeEach
    void setUp() {
        emp = Employee.builder()
                .name("홍길동")
                .salary(10_000)
                .email("aaa@naver.com")
                .hireDate(LocalDate.of(2000,1,1))
                .build();
    }

    @Test
    void save() {
        employeeJpaRepository.save(emp);

        Employee savedEmp = employeeJpaRepository.findById(emp.getId()).orElse(null);

        assertThat(savedEmp).isNotNull()
                .extracting("name")
                .isEqualTo(emp.getName());
    }

    @Test
    void findByName() {
        employeeJpaRepository.save(emp);

        List<Employee> empList = employeeJpaRepository.findByName(emp.getName());

        assertThat(empList)
                .extracting(Employee::getName)
                .contains(emp.getName());
    }

    @Test
    void findAll() {
        employeeJpaRepository.save(emp);

        List<Employee> empList = employeeJpaRepository.findAll();

        assertThat(empList).containsExactly(emp);
    }
}