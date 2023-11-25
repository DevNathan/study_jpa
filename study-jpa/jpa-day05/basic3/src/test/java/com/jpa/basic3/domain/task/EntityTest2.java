package com.jpa.basic3.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

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
        Board board = entityManager.getReference(Board.class, 1L);

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인

        log.info(board.getTitle());

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인

//        User founduser = entityManager.find(User.class, 1L);
//        founduser.getBoardList().forEach(System.out::println);
    }

    @Test
    void find2(){
        Board board = entityManager.find(Board.class, 1L);
        System.out.println("board.getClass() = " + board.getClass());
        System.out.println(board.getUser().getClass());
        System.out.println(board.getUser().getId());
    }

    @Test
    void findLazy() {
        entityManager.clear();

        Board board = entityManager.find(Board.class, 1L);
//        지연 로딩을 사용한 경우 Board만 조회하는 것을 볼 수 있다.

        log.info("is It initialized: {}", Hibernate.isInitialized(board.getUser())); // 프록시 여부 확인

        User user = board.getUser();

        log.info("is It initialized: {}", Hibernate.isInitialized(user)); // 프록시 여부 확인
//        user 객체를 가져왔으나 select가 날아가지 않는다.
//        현재 user에 담긴 객체는 프록시 객체이다(가짜 객체).

//        log.info(user.getUserName());
    }

    @Test
    void findLazy2() {
        User user = entityManager.find(User.class, 1L);
//        User를 조회하는 경우 Board는 join이 되지 않는다.
//        연관관계에서 Board처럼 [다대일]이거나 [일대일] 안 경우, 즉, 반대쪽이 [일]인 경우
//        즉시 로딩을 default로 사용한다.

//        그러나 User처럼 [일대다], [다대다]
//        즉, 반대쪽이 [다]인 경우 지연 로딩을 default로 사용한다.
        log.info("" + user.getBoardList().get(0));
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

    @Test
    void save2(){
        User user = new User();
        user.setLoginId("aaa");
        user.setPassword("1234");
        user.setAge(22);

//        entityManager.persist(user);

        Board board = new Board();
        board.setContent("안녕하세요");
        board.setTitle("반갑습니다.");
        board.setUser(user);
        entityManager.persist(board);
    }

    @Test
    void remove2(){
        Board foundBoard = entityManager.find(Board.class, 1L);
        User foundUser = entityManager.find(User.class, 1L);

        entityManager.remove(foundUser);
    }

    @Test
    void orphan(){
        User foundUser = entityManager.find(User.class, 2L);
        List<Board> boardList = foundUser.getBoardList();
        System.out.println("boardList = " + boardList);

        boardList.remove(0);
//        우리는 List의 remove()를 이용하여 게시물 엔티티 하나의 관계를 끊어주었다.
//        그ㅡ러나 이것은 엔티티를 삭제한 것이 아니라 단순히 컬렉션의 값을 하나 지운 것이다.
//        즉, DB에는 반영되지 않는다.
//        이렇게 연관관계가 끊겨버린 엔티티 객체를 고아 객체라고 한다.

//        hibernate 버그, orpahnremoval 설정시 cascadetype을 all 또는 persist로 지정할 것
    }
}