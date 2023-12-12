package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.dto.UserDto;
import com.jpa.data01.domain.embedded.Address;
import com.jpa.data01.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserQueryRepositoryTest {
    @Autowired
    UserQueryRepository userQueryRepository;

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

        userQueryRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void findUserName(){
        List<String> nameList = userQueryRepository.findUserName();
        System.out.println("nameList = " + nameList);
    }

    @Test
    void findUsersByPhone(){
        List<User> userList = userQueryRepository.findUsersByPhone();
        System.out.println("userList = " + userList);
    }

    @Test
    void findUsersByAddressDetail(){
        List<User> userList = userQueryRepository.findUsersByAddressDetail("101호");
        System.out.println("userList = " + userList);
    }

    @Test
    void findUsersByKeyword(){
        List<User> userList = userQueryRepository.findUsersByKeyword("뽀");
        System.out.println("userList = " + userList);
    }

    @Test
    void findUsersByKeywords(){
        List<User> users = userQueryRepository.findUsersByKeywords(List.of("뽀로로", "크롱"));
        System.out.println("users = " + users);
    }

    @Test
    void findAddress(){
        List<Address> address = userQueryRepository.findAddress();
        System.out.println("address = " + address);
    }

    @Test
    void findDtoByZipCode(){
        List<UserDto> dtoList = userQueryRepository.findDtoByZipCode("1");
        System.out.println("dtoList = " + dtoList);
    }

    @Test
    void findMap(){
        List<Map<String, Object>> map = userQueryRepository.findMap();
        System.out.println("map = " + map);
    }

    @Test
    void findUserByAge(){
        List<User> users = userQueryRepository.findUserByAge();
        System.out.println("users = " + users);
    }
}











