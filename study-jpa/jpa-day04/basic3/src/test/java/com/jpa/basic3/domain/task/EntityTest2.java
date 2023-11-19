package com.jpa.basic3.domain.task;

import lombok.extern.slf4j.Slf4j;
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
class EntityTest2 {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void saveUser() {
        User user = new User();
        user.setUserName("홍길동");
        user.setLoginId("asdf");
        user.setPassword("1234");
        user.setAge(23);

        entityManager.persist(user);
    }

    @Test
    void saveBoard() {
        User foundUser = entityManager.find(User.class, 1L);

        Board board = new Board();

        board.setTitle("안녕하세요");
        board.setContent("qwerasdfzxcv");
        board.setUser(foundUser);

        entityManager.persist(board);
    }

    @Test
    void find() {
//        Board board = entityManager.getReference(Board.class, 1L);

//        log.info(board.toString());

        User founduser = entityManager.find(User.class, 1L);
        founduser.getBoardList().forEach(System.out::println);
    }

    @Test
    void findLazy() {
        Board board = entityManager.find(Board.class, 1L);
//        지연 로딩을 사용한 경우 Board만 조회하는 것을 볼 수 있다.

        User user = board.getUser();
//        user 객체를 가져왔으나 select가 날아가지 않는다.
//        현재 user에 담긴 객체는 프록시 객체이다(가짜 객체).

        log.info(user.getUserName());
    }

    @Test
    void findLazy2() {
        User user = entityManager.find(User.class, 1L);
//        User를 조회하는 경우 Board는 join이 되지 않는다.
//        연관관계에서 Board처럼 [다대일]이거나 [일대일] 안 경우, 즉, 반대쪽이 [일]인 경우
//        즉시 로딩을 default로 사용한다.

//        그러나 User처럼 [일대다], [다대다]
//        즉, 반대쪽이 [다]인 경우 지연 로딩을 default로 사용한다.
        user.getBoardList().get(0);
    }

    @Test
    void update() {
        Board board = entityManager.find(Board.class, 1L);

        board.setContent("수정되었을까요?");
    }

    @Test
    void delete() {
        entityManager.remove(entityManager.find(Board.class, 1L));
    }
}