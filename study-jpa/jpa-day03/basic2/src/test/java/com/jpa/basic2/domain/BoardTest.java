package com.jpa.basic2.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
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
@Transactional
@Commit
class EntityTest {
    @PersistenceContext
    EntityManager entityManager;

//    @Test
//    void save() {
//        User user = new User();
//        user.setUserName("홍길동");
//        user.setLoginId("aaaa");
//        user.setPassword("1234");
//        user.setAge(20);
//
//        Board board1 = new Board();
//        board1.setTitle("안녕");
//        board1.setContent("하세요");
//        board1.setUser(user);
//
//        Board board2 = new Board();
//        board2.setTitle("안녕");
//        board2.setContent("하세요");
//        board2.setUser(user);
//
//        entityManager.persist(user);
//        entityManager.persist(board1);
//        entityManager.persist(board2);
//    }
//
//    @Test
//    void find() {
//        Board board = entityManager.find(Board.class, 2L);
//        User user = board.getUser(); // 객체 그래프 탐색
//
//        System.out.println("user = " + user);
//    }
//
//    @Test
//    void find2() {
//        List<Board> boardList = entityManager.createQuery("select b from Board b " +
//                        "join b.user u where u.userName = :name", Board.class)
//                .setParameter("name", "홍길동")
//                .getResultList();
//
//        for (Board board : boardList) {
//            log.info("result = {}", board);
//        }
//    }
//
//    @Test
//    void update() {
//        User user = new User();
//        user.setUserName("신사임당");
//        user.setLoginId("bbb");
//        user.setPassword("1234");
//        user.setAge(50);
//
////        새로운 회원정보 등록
//        entityManager.persist(user);
//
////        2번 게시물 조회
//        Board board = entityManager.find(Board.class, 2L);
//        board.setUser(user);
//
//        log.info("result = {}", board);
//    }
//
//    @Test
//    void remove() {
//        User user = entityManager.find(User.class, 1L);
//
//        String query = "select b from Board b where b.user.id = :userId";
//        Board board = entityManager.createQuery(query, Board.class).setParameter("userId", user.getId())
//                .getSingleResult();
//
//        board.setUser(null); // 연관관계 해제
//        entityManager.remove(user); // user 삭제 이전에 참조중인 fk 제거 우선 필수 ↑
//    }

//    ================================================================================== 일대다 테스트
//    @Test
//    void save() {
//        User user = new User();
//        user.setUserName("홍길동");
//        user.setLoginId("aaaa");
//        user.setPassword("1234");
//        user.setAge(20);
//
//        Board board1 = new Board();
//        board1.setTitle("안녕");
//        board1.setContent("하세요");
//
//        Board board2 = new Board();
//        board2.setTitle("안녕");
//        board2.setContent("하세요");
//
////        User를 기준으로 넣었으므로 User에서 Board를 넣어주는 형식.
////        원래 Board에 fk는 null 이었다가 update시켜서 값을 집어넣는다.
////        = 매우 비효율적. 차라리 이렇게 할바에는 양방향으로 설계하는 것이 맞다.
//        user.getBoardList().add(board1);
//        user.getBoardList().add(board2);
//
//        entityManager.persist(user);
//        entityManager.persist(board1);
//        entityManager.persist(board2);
//    }

    //    ======================================================================= 양방향 테스트
    @Test @DisplayName("양방향 참조")
    void save() {
        User user = new User();
        user.setUserName("홍길동");
        user.setLoginId("aaaa");
        user.setPassword("1234");
        user.setAge(20);

        Board board1 = new Board();
        board1.setTitle("안녕");
        board1.setContent("하세요");
//        주인쪽에서 fk 설정 필요
        board1.setUser(user);

        Board board2 = new Board();
        board2.setTitle("안녕");
        board2.setContent("하세요");
//        주인쪽에서 fk 설정 필요
        board2.setUser(user);

//        주인이 아닌쪽에서는 read만 가능하다. 그러므로 아래의 문장은 소용이 없음.
//        user.getBoardList().add(board1);
//        user.getBoardList().add(board2);

        entityManager.persist(user);
        entityManager.persist(board1);
        entityManager.persist(board2);

        entityManager.flush();
        entityManager.clear();

//        양방향 참조시 한쪽에서 toString 제어가 필요하다. => stackOverFlow발생할 수 있음.
        User foundUser = entityManager.find(User.class, 1L);
        List<Board> boardList = foundUser.getBoardList();
        for(Board board : boardList){
            log.info("result = {}", board);
        }

        Board newBoard = new Board();
        newBoard.setTitle("추가된 보드");
        newBoard.setContent("추가되었을까요?");
//        연관관계의 주인이 아닌 boardList를 통해 데이터를 추가하는 것은 불가능하다(오류 안나고 무시).
        boardList.add(newBoard);
    }

}