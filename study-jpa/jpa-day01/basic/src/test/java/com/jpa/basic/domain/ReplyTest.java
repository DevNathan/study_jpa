package com.jpa.basic.domain;

import com.jpa.basic.domain.type.ReplyStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class ReplyTest {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void insert(){
        Reply reply = new Reply();
        reply.setContent("hi");
        reply.setWriter("홍길동");
        reply.setStatus(ReplyStatus.PUBLIC);

        entityManager.persist(reply);
    }

    @Test
    void select(){
        Reply foundReply = entityManager.find(Reply.class, 1L);
    }

    @Test
    void update(){
        Reply foundReply = entityManager.find(Reply.class, 1L);
        foundReply.setWriter("람머스");
    }

    @Test
    void delete(){
        entityManager.remove( entityManager.find(Reply.class, 1L) );
    }

    @Test
    void selectAll(){
//        createQuery로 JPQL을 SQL로 만들어주며 getResultList를 통해 SQL을 실행시키고 List를 반환받는다.
        List<Reply> replyList = entityManager.createQuery("select r from Reply r", Reply.class).getResultList();

//        JPQL은 테이블을 알지 못한다. 여기서 from Reply는 엔티티를 의미한다.
//        엔티티의 이름은 별도로 설정하지  않으면 클래스 이름으로 자동 설정된다.
//        Reply.class는 결과의 타입을 정한다.
//        1. alias는 필수
//        2. 전체 조회 시 * 이 아닌 별칭을 사용
//        3. 특정 칼럼만 조회하고 싶다면 r.content 이런식으로 작성하면 된다.

    }

}


















