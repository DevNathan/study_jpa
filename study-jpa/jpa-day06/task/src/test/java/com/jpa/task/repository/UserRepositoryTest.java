package com.jpa.task.repository;

import com.jpa.task.domain.embedded.Address;
import com.jpa.task.domain.entity.User;
import com.jpa.task.domain.enumType.UserGender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional @Commit
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("홍길동");
        user.setLoginId("aaa");
        user.setPassword("1234");
        user.setGender(UserGender.M);
        user.setAddress(new Address("강남구", "100호", "11111"));

        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void save() {;}

    @Test
    void findById() {
        Optional<User> foundUser =
                userRepository.findById(1L);

        User user = foundUser.orElseThrow(() -> new IllegalStateException("조회 결과 없음"));
        Assertions.assertThat(user.getLoginId()).isEqualTo("aaa");
    }

    @Test
    void findByLoginId() {
    }

    @Test
    void findByLoginIdAndPassword() {
    }
}