package com.jpa.basic.domain;

import com.jpa.basic.domain.type.MemberGender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional @Commit  // JPA는 반드시 트랜잭션이 필요하다.
class MemberTest {
//    EntityManager는 JPA의 대부분의 기능을 메소드로 가지고 있는 객체이다.
//    Entity를 db에 CRUD할 수 있다.
//    EntityManager는 EntityManagerFactory를 통해서 가져올 수 있다.

//    스프링부트에서 JPA를 사용하는 경우 EntityManagerFactory를 빈으로 등록하여 가지고 있기 때문에
//    EntityManager를 스프링 컨테이너에게 주입을 받을 수 있다.
//    단, AutoWried어노테이션이 아닌 PersistenceContext라는 entityManager 전용 어노테이션을 사용한다.
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void memberTest(){
        Member member = new Member();
        member.setAge(22);
        member.setEmail("aaa@naver.com");
        member.setGender(MemberGender.MALE);
        member.setPassword("1234");
        member.setName("김철수");

//       엔티티매니저는 엔티티를 넘겨받으면 영속성 컨텍스트 내부의 [1차 캐시]라는 곳에 저장한다.
//        그리고 엔티티매니저는 쿼리도 생성을 해주는데 쿼리를 생서하게되면 영속성 컨텍스트 내부의[쓰기 지연 SQL 저장소]라는 공간에
//        SQL을 저장한다. 엔티티매니저에게 엔티티를 넘겨주고 INSERT쿼리를 생성하는 메소드가 persist()이다.
        entityManager.persist(member);

//        엔티티 매니저에게 엔티티를 넘겨주어도 DB에 저장한것이 아니다. -> 영속성 컨텍스트에 저장한것이다.
//        여기서 쿼리를 날리고 싶다면 flush를 해줘야한다.
//        flush를 하면 DB에 해당 쿼리를 반영하고, commit을해야 반영 내용을 확정짓는다.
//        그러므로 JPA를 사용하는경우 항상 트랜잭션이 필요하다.

    }

    @Test
    void insertAndSelect(){
//        엔티티 객체를 만들었지만 영속 컨텍스트에 등록되지 않은 비영속 상태이다.
        Member member = new Member();
        member.setAge(22);
        member.setEmail("BBB@naver.com");
        member.setGender(MemberGender.MALE);
        member.setPassword("1234");
        member.setName("김철수");

//        영속성 컨텍스트에 저장했으므로 영속 상태가 되었다.
        entityManager.persist(member);

//        엔티티 매니저를 이용하여 조회하는 메소드는 find()이다.
//        find를 통해 entity클래스와 pk를 알려주면 해당 엔티티와 매핑된 테이블에서 pk를 이용하여 조회를 해온다.
//        Member member1 = entityManager.find(Member.class, 1L);
//        그러나 실행 후 로그를 살펴보면 select쿼리는 보이지 않는다.
//        이유는 commit을 하기전에는 DB에 저장되지 않은 상태이다.
//        현재 엔티티는 [1차 캐시]에 존재한다.
//        엔티티매니저는 find를 할 때 바로 DB를 조회하는것이 아니라, 우선 영속성 컨텍스트에 존재하는지 확인을 한 후 없으면 DB를 조회한다.
//        그렇기 때문에 select쿼리가 보이지 않는 것이다.

//        log.info("result : {}", member1);
//        log.info("같니? : {}", member == member1);
    }

    @Test
    void insertAndSelect2(){
        Member member = new Member();
        member.setAge(22);
        member.setEmail("aaa@naver.com");
        member.setGender(MemberGender.MALE);
        member.setPassword("1234");
        member.setName("김철수");

//        select쿼리를 보기 위해서는 우리가 persist()로 영속성 컨텍스트에 보낸 엔티티와 Insert쿼리를 실행시켜 DB에 반영하면 된다.
//        즉, 강제로 flush()를 시켜줘야 한다.
        entityManager.persist(member);

        entityManager.flush();
//        실행해보면 여전히 select쿼리는 보이지 않는다.
//        이유는 우리가 flush()를 하여도 엔티티는 여전히 영속성 컨텍스트에 남아있기 때문이다.(flush()가 영속성 컨텍스트를 비워주지 않는다!!)
//        그렇기 때문에 우리는 영속성 컨텍스트를 비워주어야 select를 볼 수 있다.
//        entityManager.clear();
        entityManager.detach(member); // 특정 엔티티만 영속성 컨텍스트에서 제외시킬 때 사용
//        이렇게 저장되었다가 분리된 엔티티의 상태를 준영속 상태라고 한다.

        Member member1 = entityManager.find(Member.class, 1L);

        log.info("result : {}", member1);
        log.info("같니? : {}", member == member1);
    }

    @Test
    void update(){
        Member member = entityManager.find(Member.class, 1L);
//        update쿼리를 실행시킬 때는 별도의 메소드가 존재하지 않는다.
//        영속상태의 엔티티의 데이터를 수정하게되면 자동으로 update가 된다.
//        영속상태의 엔티티가 처음 등록될 때의 상태값을 스냅샷으로 저장하고 flush가 될때 현재 상태의 값과 비교하여
//        자동 update가 된다. 이를 변경감지(dirty checking)이라고 한다.
        member.setName("홍길동");
    }

    @Test
    void delete(){
        Member member = entityManager.find(Member.class, 1L);
        entityManager.remove(member);
    }

    @Test
    void merge(){
        Member foundMember = entityManager.find(Member.class, 2L); // 영속상태
        entityManager.detach(foundMember); // 준영속

        foundMember.setName("홍길동");
        Member mergedMember = entityManager.merge(foundMember);

        mergedMember.setName("김영희");
    }

}










