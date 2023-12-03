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
class BoardTest {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void testCase(){
        User user = new User();
        user.setLoginId("test");
        user.setPassword("1234");
        user.setName("test");
        user.setGender(UserGender.M);
        user.setAddress(new Address("test", "test", "12345"));

        Board board = new Board();
        board.setTitle("test");
        board.setContent("test");
        board.setUser(user);

        entityManager.persist(board);
        entityManager.flush();
        entityManager.clear();

    }
}