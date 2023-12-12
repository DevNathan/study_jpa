package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.dto.BookDto;
import com.jpa.data01.domain.entity.Book;
import com.jpa.data01.domain.type.BookCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class BookQueryRepositoryTest {
    @Autowired BookQueryRepository bookQueryRepository;

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

        bookQueryRepository.saveAll(List.of(book1, book2, book3));
    }

    @Test
    void findNameList() {
        List<String> nameList = bookQueryRepository.findNameList();
        System.out.println("nameList = " + nameList);
    }

    @Test
    void findBooksChNull(){
        List<Book> bookList = bookQueryRepository.findBooksChNull();
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findBookQuery(){
        List<Book> bookList = bookQueryRepository.findBookQuery(BookCategory.IT, LocalDateTime.of(2000, 1, 1, 12, 30));
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findByIn(){
        List<Book> bookList = bookQueryRepository.findByIn(List.of(BookCategory.IT, BookCategory.HISTORY));
        System.out.println("bookList = " + bookList);
    }

    @Test
    void findTotalPriceOfNovel(){
        int totalPrice = bookQueryRepository.findTotalPriceOfNovel();
        System.out.println("totalPrice = " + totalPrice);

    }

    @Test
    void countIdOfGroup(){
        List<Integer> countList = bookQueryRepository.countIdOfGroup();
        System.out.println("countList = " + countList);
    }

    @Test
    void findDtoById(){
        BookDto bookDto = bookQueryRepository.findDtoById(1L);
        System.out.println("bookDto = " + bookDto);
    }

    @Test
    void findDtoByPrice(){
        List<BookDto> bookDtoList = bookQueryRepository.findDtoByPrice(10_000);
        System.out.println("bookDtoList = " + bookDtoList);
    }

    @Test
    void findAvgPriceOfCategory(){
        Map<String, Object> map = bookQueryRepository.findAvgPriceOfCategory(BookCategory.IT);
        System.out.println("map = " + map);
    }

    @Test
    void findAvgPricePerCategory(){
        List<Map<String, Object>> mapList = bookQueryRepository.findAvgPricePerCategory();
        mapList.forEach(System.out::println);
    }

    @Test
    void findAllNameWithPrice(){
        List<String> list = bookQueryRepository.findAllNameWithPrice();
        System.out.println("list = " + list);
    }

    @Test
    void findAllNameWithPrice2(){
        List<String> list = bookQueryRepository.findAllNameWithPrice2();
        System.out.println("list = " + list);
    }

    @Test
    void findBookByReleaseDateYYYY(){
        List<Book> bookList = bookQueryRepository.findBookByReleaseDateYYYY("2010");
        System.out.println("bookList = " + bookList);
    }

    @Test
    void nativeFind(){
        List<Book> bookList = bookQueryRepository.nativeFind();
        System.out.println("bookList = " + bookList);
    }
}















