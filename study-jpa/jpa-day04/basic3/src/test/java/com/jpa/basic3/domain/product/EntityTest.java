package com.jpa.basic3.domain.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
@Transactional @Commit
class EntityTest {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void save() {
        Clothes clothes = new Clothes();

        clothes.setName("후드");
        clothes.setPrice(50_000);
        clothes.setSize(100);
        clothes.setColor("red");

        entityManager.persist(clothes);

        Book book = new Book();

        book.setName("백과사전");
        book.setPrice(25000);
        book.setAuthor("홍길동");
        book.setPublisher("나비조아");

        entityManager.persist(book);
    }
    
    @Test
    void find(){
        Book book = entityManager.find(Book.class, 2L);

        System.out.println("book = " + book.getName());
    }

    @Test
    void update(){
        Clothes clothes = entityManager.find(Clothes.class, 1L);

        clothes.setPrice(80_000);
    }

    @Test
    void delete(){
        entityManager.remove(entityManager.find(Book.class, 2L));
    }
}