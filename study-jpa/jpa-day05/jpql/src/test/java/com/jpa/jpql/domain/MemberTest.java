package com.jpa.jpql.domain;

import com.jpa.jpql.dto.MemberDTO;
import com.jpa.jpql.embedded.Address;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;


@SpringBootTest
@Slf4j
@Transactional
@Commit
class MemberTest {
    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setup(){
        Member member = new Member();
        Address address = new Address("강남구", "1011호", "11122");
        member.setName("홍길동");
        member.setAddress(address);

        entityManager.persist(member);

        Member member2 = new Member();
        Address address2 = new Address("강북구", "101호", "12222");
        member2.setName("신사임당");
        member2.setAddress(address2);

        entityManager.persist(member2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void save(){}

    @Test
    void jpql(){
        String query = "select m from Member m " +
                "where m.address.address like '%강남구%'";

        List<Member> resultList = entityManager.createQuery(query, Member.class).getResultList();

        for(Member result : resultList){
            System.out.println("result = " + result);
        }
    }

    @Test
    void nativeQuery(){
        String query = "SELECT * FROM JPA_MEMBER WHERE NAME = '홍길동'";

        List<Member> resultList = entityManager.createNativeQuery(query, Member.class).getResultList();

        for(Member result : resultList){
            System.out.println("result = " + result);
        }
    }

    @Test
    void Criteria(){
//        criteria 사용 준비
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        어떤 엔티티를 조회용으로 사용할 것인지 설정한다.
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);

//        조회 클래스, from 절을 세팅하고 criteria로 쿼리를 만들 때 이 객체를 사용한다.
        Root<Member> m = query.from(Member.class);

//        쿼리 생성하기
        CriteriaQuery<Member> criteriaQuery = query.select(m).where(criteriaBuilder.equal(m.get("name"), "홍길동"));

        List<Member> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        for(Member result : resultList){
            System.out.println("result = " + result);
        }
    }

    @Test
    void queryDSL(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QMember member = QMember.member;

//        쿼리 생성
        JPAQuery<Member> query = queryFactory.selectFrom(member)
                .where(member.address.address.like("%강%"));

//        쿼리 실행
        List<Member> members = query.fetch();

        for(Member showMember : members){
            System.out.println("result = " + showMember);
        }
    }

    @Test
    void jpql2(){
//        createQuery()의 결과는 TypedQuery가 만들어진다.(여태 우리가 사용한 방식
        //language=JPAQL
        String query = "select m from Member m where m.name='홍길동'";
        TypedQuery<Member> typedQuery = entityManager.createQuery(query, Member.class);
        List<Member> resultList = typedQuery.getResultList();

//        id만 가져오고 싶은 경우에 select id를 명시하고 id의 타입을 Longg으로 반환하도록 반환타입을 설정하면 된다.
        TypedQuery<Long> typedQuery1 = entityManager.createQuery("select m.id from Member m where m.name='홍길동'", Long.class);
        List<Long> resultList1 = typedQuery1.getResultList();

//        타입을 설정하지 않거나 그럴수 없는 경우
//        Query 타입이 반환된다.
        Query query1 = entityManager.createQuery("select m.id, m.name from Member m where m.address.address like '%강%'");

        List<Object[]> resultList2 = query1.getResultList();

        resultList2.forEach(ele -> {
            System.out.println(ele[0]);
            System.out.println(ele[1]);
        });
        System.out.println("=======");
        resultList2.stream().map(Arrays::toString).forEach(System.out::println);

        //language=JPAQL
        String sql = "select new com.jpa.jpql.dto.MemberDTO(m.id, m.name) from Member m where m.address.address like '%강%'";
        TypedQuery<MemberDTO> typedQuery2 = entityManager.createQuery(sql, MemberDTO.class);
        List<MemberDTO> resultList3 = typedQuery2.getResultList();

        assertThat(resultList3.size()).isNotEqualTo(0);
    }

}