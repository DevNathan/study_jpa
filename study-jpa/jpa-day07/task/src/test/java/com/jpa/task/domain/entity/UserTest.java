package com.jpa.task.domain.entity;

import com.jpa.task.domain.embedded.Address;
import com.jpa.task.domain.enumType.UserGender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserTest {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void defaultTest(){
        User user = new User();
        user.setLoginId("aaa");
        user.setPassword("1234");
        user.setName("김철수");
        user.setAddress(new Address("강남구", "100호", "11111"));
//        user.setGender(UserGender.M);

        entityManager.persist(user);

        entityManager.flush();
        entityManager.clear();
        User user1 = entityManager.find(User.class, 1L);
        System.out.println(user1);
    }

}









