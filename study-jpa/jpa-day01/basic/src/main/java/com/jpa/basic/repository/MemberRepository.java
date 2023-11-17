package com.jpa.basic.repository;

import com.jpa.basic.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Member member){
        entityManager.persist(member);
    }

    public Member findOne(Long id){
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll(){
        return entityManager.createQuery("select m from Member m", Member.class).getResultList();
    }

    public void delete(Member member){
        entityManager.remove(member);
    }

}
