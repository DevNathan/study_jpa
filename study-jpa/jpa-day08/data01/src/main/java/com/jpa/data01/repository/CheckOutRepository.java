package com.jpa.data01.repository;

import com.jpa.data01.domain.entity.CheckOut;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CheckOutRepository {
    private final EntityManager em;

    public CheckOut save(CheckOut checkOut){
        em.persist(checkOut);
        return checkOut;
    }

    public Optional<CheckOut> findById(Long checkOutId){
        return Optional.ofNullable(em.find(CheckOut.class, checkOutId));
    }

    public List<CheckOut> findAll(){
        String query = "select b from CheckOut b";
        return em.createQuery(query, CheckOut.class).getResultList();
    }

    public void delete(CheckOut checkOut){
        em.remove(checkOut);
    }
}
