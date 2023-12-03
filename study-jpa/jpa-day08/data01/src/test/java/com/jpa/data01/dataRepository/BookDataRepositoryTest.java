package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.entity.Book;
import com.jpa.data01.domain.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class BookDataRepositoryTest {
    @Autowired
    BookDataRepository bookDataRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setUp(){
        Book book1 = new Book();
        book1.setCategory(BookCategory.IT);
        book1.setName("JPA");
        book1.setPrice(10_000);
        book1.setReleaseDate(LocalDate.of(2023, 11, 11));

        Book book2 = new Book();
        book2.setCategory(BookCategory.NOVEL);
        book2.setName("해리포터");
        book2.setPrice(20_000);
        book2.setReleaseDate(LocalDate.of(2000, 1, 12));

        Book book3 = new Book();
        book3.setCategory(BookCategory.HISTORY);
        book3.setName("세계로");
        book3.setPrice(15_000);
        book3.setReleaseDate(LocalDate.of(2010, 7, 23));

//        List.of(book1, book2, book3).forEach(bookDataRepository::save);
        bookDataRepository.saveAll(List.of(book1, book2, book3));
        em.flush();
        em.clear();
    }


    @Test
    void save(){
        Book book = new Book();
        book.setName("test");
        book.setPrice(10_000);
        book.setCategory(BookCategory.IT);
        book.setReleaseDate(LocalDate.of(200, 10, 10));

        bookDataRepository.save(book);

        System.out.println("proxy : " + bookDataRepository.getClass());
    }

    @Test
    void find(){
        bookDataRepository.findById(2L).ifPresent(System.out::println);
        bookDataRepository.findAll().forEach(System.out::println);
    }

    @Test
    void delete(){
        bookDataRepository.deleteById(1L);
        bookDataRepository.deleteAll();
    }

}










