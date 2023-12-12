package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.dto.BookDto;
import com.jpa.data01.domain.entity.Book;
import com.jpa.data01.domain.type.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface BookQueryRepository extends JpaRepository<Book, Long> {
//    쿼리 메소드를 사용하면 이름을 기반으로 쿼리를 쉽게 만들 수 있다는 장점이 있으나
//    다음과 같은 단점도 존재한다.
//    1. 조건이 많아지면 메소드의 이름이 너무 길어져서 가독성이 떨어진다.
//    2. 복잡한 쿼리를 만들기에는 기능이 부족하다.
//    이러한 한계를 극복하는 방법이 몇 가지 존재한다.

//    @Query 활용하기
    @Query("select b.name from Book b")
    List<String> findNameList();

//    jpql에서도 is null / is not null을 지원한다.
    @Query("select b from Book b where b.checkOut is null")
    List<Book> findBooksChNull();

//    매개변수와 jpql의 파라미터를 바인딩하려면 동일한 이름을 사용하면 된다.
//    또는 @Param() 어노테이션을 이용하여 직접 바인딩해줄 수 있다.
    @Query("select b from Book b where b.category = :category and b.modifiedDate > :modifiedDate")
    List<Book> findBookQuery(@Param("category") BookCategory category,
                             @Param("modifiedDate") LocalDateTime modifiedDate);

//    in 을 사용할 수 있다. (매개변수를 Collection<T>타입 / 자식타입 List, Set도 사용 가능)
    @Query("select b from Book b where b.category in :categories")
    List<Book> findByIn(@Param("categories")Collection<BookCategory> categories);

//    JPQL은 집계함수를 모두 제공한다.
    @Query("select sum(b.price) from Book b where b.category = 'NOVEL'")
    int findTotalPriceOfNovel();

//    group by 와 having을 지원한다.
    @Query("select count(b.id) from Book b group by b.category having b.id < 10")
    List<Integer> countIdOfGroup();

//    dto로 반환을 받을 수 있다.
    @Query("select " +
            "new com.jpa.data01.domain.dto.BookDto(b.id, b.category, b.name, b.price, b.releaseDate) " +
            "from Book b where b.id = :id")
    BookDto findDtoById(@Param("id") Long bookId);

    @Query("select " +
            "new com.jpa.data01.domain.dto.BookDto(b.id, b.category, b.name, b.price, b.releaseDate) " +
            "from Book b where b.price > :price")
    List<BookDto> findDtoByPrice(@Param("price") Integer price);

//    Map으로 반환을 받을 수 있다.
//    map을 사용하는 경우 별칭이 key로 설정된다.
    @Query("select new Map(b.category as category, avg(b.price) as avg) from Book b group by b.category having b.category = :category")
    Map<String, Object> findAvgPriceOfCategory(@Param("category") BookCategory category);

//    Map으로 반환하라 때 여러 행을 반환해야한다면 List로 감싼다.
    @Query("select new Map(b.category as category, avg(b.price) as avg) from Book b group by b.category")
    List<Map<String, Object>> findAvgPricePerCategory();

//    JPQL에서 기본 제공하는 함수 중 CONCAT은 연결처리를 해준다.
    @Query("select concat(b.name, '의 가격 : ', b.price, '원') from Book b")
    List<String> findAllNameWithPrice();

    @Query("select b.name || '의 가격 : ' || b.price || '원' from Book b")
    List<String> findAllNameWithPrice2();

//    표준 SQL명령어가 아닌 특정 벤더사의 명령어를 방언이라고 한다.
//    오라클에서 지원하는 형변환 함수 TO_CHAR(SYSDATE, 'YYYY')는 방언이기 때문에 별도의 등록이 필요하다.
//    하이버네이트는 DB별 기본적인 함수들을 등록해놨기 때문에 바로 사용이 가능하다.
//    사용할 때는 function('함수명', 인자1, 인자2, 인자3, .....) 으로 사용한다.
//    @Query("select b from Book b " +
//            "where function('to_char', b.releaseDate, 'YYYY') = :year")

//    방언을 직접 호출하여 사용하는것도 가능하지만 DB벤더사가 변경되면 문제가 생길 수 있다.
//    @Query("select b from Book b " +
//            "where to_char(b.releaseDate, 'YYYY') = :year")
    @Query("select b from Book b where year(b.releaseDate) = :year")
    List<Book> findBookByReleaseDateYYYY(@Param("year") String year);

//    nativeQuery를 사용할 수 있다.
    @Query(value = "SELECT * FROM JPA_BOOK", nativeQuery = true)
    List<Book> nativeFind();
}




















