package com.jpa.basic.domain.type;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberGenderTest {

    @Test
    void test(){
        MemberGender m = MemberGender.MALE;
        System.out.println(m.ordinal());
    }
}














