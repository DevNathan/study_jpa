package com.jpa.data01.repository;

import com.jpa.data01.domain.embedded.Address;
import com.jpa.data01.domain.entity.Book;
import com.jpa.data01.domain.entity.CheckOut;
import com.jpa.data01.domain.entity.User;
import com.jpa.data01.domain.type.BookCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class RepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckOutRepository checkOutRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setUp() {
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

        Address address1 = new Address("서울특별시 강남구", "101호", "11111");
        Address address2 = new Address("서울특별시 송파구", "202호", "22222");
        Address address3 = new Address("서울특별시 노원구", "303호", "33333");


        User user1 = new User();
        user1.setName("뽀로로");
        user1.setBirth(LocalDate.of(2000, 1, 1));
        user1.setPhone("010111111111");
        user1.setAddress(address1);

        User user2 = new User();
        user2.setName("루피");
        user2.setBirth(LocalDate.of(2010, 10, 19));
        user2.setPhone("01022222222");
        user2.setAddress(address2);

        User user3 = new User();
        user3.setName("크롱");
        user3.setBirth(LocalDate.of(2013, 5, 30));
        user3.setPhone("01033333333");
        user3.setAddress(address3);

        List.of(book1, book2, book3).forEach(bookRepository::save);
        List.of(user1, user2, user3).forEach(userRepository::save);

        CheckOut checkOut1 = new CheckOut();
        checkOut1.setBook(book1);
        checkOut1.setUser(user1);

        CheckOut checkOut2 = new CheckOut();
        checkOut2.setBook(book2);
        checkOut2.setUser(user2);

        CheckOut checkOut3 = new CheckOut();
        checkOut3.setBook(book3);
        checkOut3.setUser(user3);

        List.of(checkOut1, checkOut2, checkOut3).forEach(checkOutRepository::save);

        em.flush();
        em.clear();

//        System.out.println("check : " + checkOut1.getBook());
//        System.out.println("check : " + book1.getCheckOut());
    }

    @Test
    void save(){
        Book book1 = new Book();
        book1.setCategory(BookCategory.IT);
        book1.setName("JPA");
        book1.setPrice(10_000);
        book1.setReleaseDate(LocalDate.of(2023, 11, 11));

        Address address1 = new Address("서울특별시 강남구", "101호", "11111");

        User user1 = new User();
        user1.setName("뽀로로");
        user1.setBirth(LocalDate.of(2000, 1, 1));
        user1.setPhone("010111111111");
        user1.setAddress(address1);

        CheckOut checkOut1 = new CheckOut();
        checkOut1.setBook(book1);
        checkOut1.setUser(user1);

        em.persist(book1);
        em.persist(user1);
        em.persist(checkOut1);

        System.out.println("check" + checkOut1.getBook());
        System.out.println("check : " + book1.getCheckOut());

//        book1.setCheckOut(checkOut1);
//        System.out.println("check : " + book1.getCheckOut());


//        em.flush();
//        em.clear();
//
//        Optional<Book> foundBook = bookRepository.findById(4L);
//        foundBook.ifPresent(b -> System.out.println(b.getCheckOut()));
    }

    @Test
    void find(){
        Optional<Book> foundBook = bookRepository.findById(1L);
        foundBook.ifPresent(book -> System.out.println("find : " + book.getCheckOut()));
    }

    @Test
    void delete(){
        Optional<Book> foundBook = bookRepository.findById(1L);
        foundBook.ifPresent(bookRepository::delete);
    }

    @Test
    void findBy(){
    }
}





















