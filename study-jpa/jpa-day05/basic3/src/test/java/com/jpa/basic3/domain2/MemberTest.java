package com.jpa.basic3.domain2;

import com.jpa.basic3.embedded.Address;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional @Commit
class MemberTest {
    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setup(){
        Member member = new Member();
        Address address = new Address("강남구", "1011호", "11122");

        member.setName("홍길동");
//        address.setAddress("강남구");
//        address.setAddressDetail("1011호");
//        address.setZipcode("11122");
        member.setAddress(address);

        entityManager.persist(member);

        Member member2 = new Member();
        Address address2 = new Address("강북구", "101호", "12222");

        member2.setName("신사임당");
//        address.setAddress("강북구");
//        address.setAddressDetail("101호");
//        address.setZipcode("12222");
        member2.setAddress(address2);

        entityManager.persist(member2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void save(){}

    @Test
    void equals(){
        Address address = new Address("강북구", "101호", "12222");
        Member foundMember = entityManager.find(Member.class, 2L);
        System.out.println("isTrue : " + address.equals(foundMember.getAddress()));
    }
}