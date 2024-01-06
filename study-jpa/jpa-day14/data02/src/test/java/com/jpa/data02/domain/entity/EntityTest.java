package com.jpa.data02.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntityTest {

    @Test
    void entityTest(){
        Employee emp = Employee.builder()
                .salary(100)
                .hireDate(LocalDate.of(2000, 10, 10))
                .build();

        System.out.println("emp = " + emp);
    }

}