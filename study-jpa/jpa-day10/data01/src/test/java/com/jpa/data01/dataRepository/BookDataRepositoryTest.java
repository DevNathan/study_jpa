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
import java.util.Optional;

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

    @Test
    void findBy(){
        List<Book> bookList = bookDataRepository.findBy();
        List<Book> bookList2 = bookDataRepository.findBooksBy();
    }

    @Test
    void findByName(){
        List<Book> bookList = bookDataRepository.findByName("해리포터");

    }

    @Test
    void findByPrice(){
        List<Book> bookList = bookDataRepository.findTestByPrice(10_000);
    }

    @Test
    void emTest(){
        em.createQuery("select b from Book b where b.id = 1000").getSingleResult();
    }

    @Test
    void findOptionalTest(){
        Optional<Book> foundBook = bookDataRepository.findOptionalByName("");
        System.out.println(foundBook.isPresent());

        Book book = bookDataRepository.findBookByName("");
        System.out.println("book = " + book);
    }

    @Test
    void findByPriceGreaterThan(){
        List<Book> bookList = bookDataRepository.findByPriceGreaterThan(10_000);
        List<Book> bookList2 = bookDataRepository.findByPriceLessThan(10_000);
    }

    @Test
    void findByReleaseDateGreaterThanEqual(){
        List<Book> bookList = bookDataRepository.findByReleaseDateGreaterThanEqual(LocalDate.of(2000, 01, 21));
        System.out.println(bookList);
    }

    @Test
    void findByReleaseDateAfter(){
        List<Book> bookList = bookDataRepository.findByReleaseDateAfter(LocalDate.of(2001, 12, 12));
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByNameLike(){
        List<Book> bookList = bookDataRepository.findByNameLike("J");
        List<Book> bookList2 = bookDataRepository.findByNameNotLike("J");
        System.out.println("bookList = " + bookList);
        System.out.println("bookList2 = " + bookList2);
    }

    @Test
    void findByNameContaining(){
        List<Book> bookList = bookDataRepository.findByNameContaining("J");
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByNameStartingWith(){
        List<Book> bookList = bookDataRepository.findByNameStartingWith("J");
        List<Book> bookList1 = bookDataRepository.findByNameStartsWith("J");
        System.out.println("bookList1 = " + bookList1);
    }

    @Test
    void findByPriceIsNull(){
        List<Book> bookList = bookDataRepository.findByPriceIsNull();
        System.out.println("bookList = " + bookList);
    }

    @Test
    void between(){
        List<Book> bookList = bookDataRepository.findByPriceBetween(10_000, 12_000);
        System.out.println("bookList = " + bookList);
    }

    @Test
    void inTest(){
        List<Book> bookList = bookDataRepository.findByNameIn(List.of("해리포터", "JPA", "TEST"));
        System.out.println("bookList = " + bookList);
    }

    @Test
    void notInTest(){
        List<Book> bookList = bookDataRepository.findByNameNotIn(List.of("해리포터", "JPA", "TEST"));
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByNameAndPrice(){
        List<Book> bookList = bookDataRepository.findByNameAndPrice("JPA", 10_000);
        System.out.println("bookList = " + bookList);
    }

    @Test
    void emTest2(){
        List<Book> bookList = em.createQuery("select b from Book b where b.price = ?1", Book.class)
                .setParameter(1, 10_000)
                .getResultList();

        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByPriceGreaterThanOrderByIdDesc(){
        List<Book> bookList = bookDataRepository.findByPriceGreaterThanOrderByIdDesc(10_000);
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByNameIgnoreCaseAndPrice(){
        List<Book> jpa = bookDataRepository.findByNameIgnoreCaseAndPrice("jpa", 10_000);
        System.out.println("jpa = " + jpa);
    }

    @Test
    void findByNameContainingIgnoreCase(){
        List<Book> bookList = bookDataRepository.findByNameContainingIgnoreCase("j");
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findDistinctByPrice(){
        List<Book> bookList = bookDataRepository.findDistinctByPrice(10_000);
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findTop3ByName(){
        List<Book> jpa = bookDataRepository.findTop3ByName("JPA");
        System.out.println("jpa = " + jpa);
    }

    @Test
    void findTop2ByOrderByPriceDesc(){
        List<Book> bookList = bookDataRepository.findTop2ByOrderByPriceDesc();
        System.out.println("bookList = " + bookList);
    }

    @Test
    void countByReleaseDateAfter(){
        long count = bookDataRepository.countByReleaseDateAfter(LocalDate.of(2001, 10, 10));
        System.out.println("count = " + count);
    }

    @Test
    void existsByCategory(){
        boolean isTrue = bookDataRepository.existsByCategory(BookCategory.IT);
        System.out.println("isTrue = " + isTrue);
    }

    @Test
    void deleteByReleaseDateAfter(){
        bookDataRepository.deleteByReleaseDateAfter(LocalDate.of(2001, 10, 10));

    }
}










