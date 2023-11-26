package com.jpa.jpql.domain;

import com.jpa.jpql.dto.MemberDTO;
import com.jpa.jpql.embedded.Address;
import com.querydsl.core.types.Projections;
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
import org.junit.jupiter.api.DisplayName;
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

        Board board = new Board();
        board.setTitle("안녕하세요");
        board.setContent("반갑습니다.");
        board.setMember(member);

        entityManager.persist(board);

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

    @Test
    void jpql3(){
        String query1 = "select b from Board b join b.member m where m.name = :name";
//        join을 사용하면 자동으로 fk, pk가 일치하는 조건의 on절이 생성된다.
//        getResultList()는 조회결과가 0, 1, 2이상 이여도 상관 없으나
//        getSingleResult()는 조회결과가 반드시 1개여야 한다. 0건이거나 2건 이상이면 예외가 발생한다.
        Board singleResult = entityManager.createQuery(query1, Board.class)
                .setParameter("name", "홍길동")
                .getSingleResult();

        System.out.println("singleResult = " + singleResult);

//        외부 조인 쿼리

        String query2 = "select b from Board b left join b.member m where m.name = :name";
        List<Board> resultList = entityManager.createQuery(query2, Board.class)
                .setParameter("name", "신사임당")
                .getResultList();

        resultList.forEach(System.out::println);

//        직접 on절 사용하기
//        식별자 확인 조건은 유지되고, on절에 작성한 조건이 추가 되는 것.
        String query3 = "select b from Board b join b.member m on m.name = b.content";
        List<Board> resultList1 = entityManager.createQuery(query3, Board.class).getResultList();

        resultList1.forEach(System.out::println);
    }

    @Test @DisplayName("패치 조인 테스트")
    void fetchTest(){
//        지연로딩 처리가 되어있기 때문에 실제 Member를 사용하는 시점에서 별도의 select를 실행한다.
//        Board foundBoard = entityManager.find(Board.class, 1L);
//        foundBoard.getMember().getName();

//        패치조인은 N+1 문제를 해결해준다 .
//        JPQL에서 성능 최적화를 위해 제공하는 조인이다.
//        일반적인 join은 쿼리가 실행되는 순간 모든 데이터를 가져와 영속성 컨텍스트에 인티티를 등록하게 된다.
//        지연 로딩은 해당 객체를 사용하는 시점에 조회하여 성능을 향상시킨다고 했었다.
//        이런 지연로딩을 무시하고 한번에 연관된 entity를 가져와 영속화시키는 방법이 패치 조인이다.
        String query = "select b from Board b join fetch b.member m";
        List<Board> resultList = entityManager.createQuery(query, Board.class).getResultList();
        resultList.forEach(System.out::println);
    }

    @Test
    void queryDSL2(){
//        queryDSL은 개발자가 entity로 설정한 클래스들을 Q타입으로 자동 생성한다.
//        Q타입을 이용하여 쿼리를 작성할 수 있다.
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMember member = QMember.member; // 기본으로 만들어주는 객체 사용
        QBoard board = QBoard.board;

        List<Member> members = queryFactory.select(member)
                .from(member)
                .join(member.boards, board)
                .fetch();

        members.forEach(System.out::println);

//        Q타입 생성하는 방법2
        QMember member2 = new QMember("m"); // 직접 만들어 사용하기(별칭을 지정할 수 있다.)
        QBoard board2 = new QBoard("b"); // 이 별칭은 내부적으로 사용되므로 신경쓸 필요 없다.

        List<Member> members2 = queryFactory.select(member2)
                .from(member2)
                .join(member2.boards, board2)
                .where(member2.id.gt(0).and(member2.name.eq("홍길동")))
                .orderBy(member2.id.desc())
                .fetch();

        members2.forEach(System.out::println);

//        QueryDSL을 이용한 페이징 처리
        queryFactory.select(member2)
                .from(member2)
                .join(member2.boards, board2).fetchJoin()
                .orderBy(member2.id.desc())
                .offset(0).limit(10) // offset : 몇 번째 결과부터 가져올지 설정, limit : 몇 개 가져올지 설정
                .fetch();
    }

    @Test
    void queryDSLTest3(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMember member = QMember.member;

//        조회 대상(프로젝션)이 1개인 경우
        List<String> names = queryFactory.select(member.name).from(member)
                .where(member.address.addressDetail.eq("101호"))
                .fetch();

//        프로젝션이 여러개인 경우 DTO를 사용한다.
//        1. DTO의 setter를 사용하는 방법
        List<MemberDTO> memberDTOS = queryFactory.select(
                Projections.bean(MemberDTO.class, member.id, member.name)
        ).from(member).fetch();

        memberDTOS.forEach(System.out::println);

//        2. DTO의 생성자를 사용하는 방법
        List<MemberDTO> memberDTOS2 = queryFactory.select(
                Projections.constructor(MemberDTO.class, member.id, member.name)
        ).from(member).fetch();

        memberDTOS2.forEach(System.out::println);
    }
}