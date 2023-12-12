package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.embedded.Address;
import com.jpa.data01.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserDataRepositoryTest {
    @Autowired
    UserDataRepository userDataRepository;

    @BeforeEach
    void setUp() {
        Address address1 = new Address("강남구", "101호", "11111");
        Address address2 = new Address("송파구", "202호", "22222");
        Address address3 = new Address("노원구", "303호", "33333");


        User user1 = new User();
        user1.setName("뽀로로");
        user1.setBirth(LocalDate.of(2000, 1, 1));
        user1.setPhone("01011111111");
        user1.setAddress(address1);

        User user2 = new User();
        user2.setName("루피");
        user2.setBirth(LocalDate.of(2010, 10, 19));
        user2.setPhone("01022222222");
        user2.setAddress(address2);

        User user3 = new User();
        user3.setName("크롱");
        user3.setBirth(LocalDate.of(2013, 5, 30));
        user3.setPhone("01033333333");
        user3.setAddress(address3);

        userDataRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void findByPhone() {
        Optional<User> foundUser = userDataRepository.findByPhone("01011111111");
        foundUser.ifPresent(System.out::println);
    }

    @Test
    void findByBirthBefore() {
        List<User> userList = userDataRepository.findByBirthBefore(LocalDate.of(2010, 1, 1));
        System.out.println("userList = " + userList);
    }

    @Test
    void findByNameContaining() {
        List<User> userList = userDataRepository.findByNameContaining("뽀");
        System.out.println("userList = " + userList);
    }

//    ============

    @Test
    void countByNameContaining(){
        long count = userDataRepository.countByNameContaining("루");
        System.out.println("count = " + count);
    }

    @Test
    void existsByPhone(){
        boolean isTrue = userDataRepository.existsByPhone("01022222222");
        System.out.println("isTrue = " + isTrue);
    }

    @Test
    void findByNameContainingAndPhoneContainingOrderByIdDesc(){
        List<User> userList = userDataRepository.findByNameContainingAndPhoneContainingOrderByIdDesc("루", "010");
        System.out.println("userList = " + userList);
    }
    @Test
    void findByNameIn(){
        List<User> userList = userDataRepository.findByNameIn(List.of("루피", "크롱"));
        System.out.println("userList = " + userList);
    }
    @Test
    void findByBirthNotNullAndIdBetween(){
        List<User> userList = userDataRepository.findByBirthNotNullAndIdBetween(2L, 3L);
        System.out.println("userList = " + userList);
    }
}









