package com.jpa.task.repository;

import com.jpa.task.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public User save(User user){
        entityManager.persist(user);
//        persist로 영속화를 하면 user객체에 식별자가 생긴다.
//        이 식별자를 유용하게 사용할 수 있으므로 user를 반환처리한다.
        return user;
    }

    public Optional<User> findById(Long userId){
        return Optional.ofNullable(entityManager.find(User.class, userId));
    }

    public Optional<User> findByLoginId(String loginId){
        String query = "SELECT u FROM User u WHERE u.loginId = :loginId";
        List<User> resultList = entityManager.createQuery(query, User.class)
                .setParameter("loginId", loginId)
                .getResultList();

//        stream에서 지원하는 findAny()
//        스트림이 가진 데이터 중 아무거나 하나를 반환한다.
//        스트림에 데이터가 없는 경우 null이 반환되는데 옵셔널로 감싸서 반환된다.
        return resultList.stream().findAny();
    }

    public Optional<Long> findByLoginIdAndPassword(String loginId, String password){
        String query = "SELECT u.id FROM User u WHERE u.loginId = :loginId AND u.password = :password";
        List<Long> resultList = entityManager.createQuery(query, Long.class)
                .setParameter("loginId", loginId)
                .setParameter("password", password)
                .getResultList();

        return resultList.stream().findAny();
    }
}










