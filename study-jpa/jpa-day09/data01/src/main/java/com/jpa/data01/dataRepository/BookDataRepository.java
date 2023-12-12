package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.entity.Book;
import com.jpa.data01.domain.type.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookDataRepository extends JpaRepository<Book, Long> {
// Spring Data JPA는 쿼리 메소드를 지원해준다.
//    1. 쿼리 메소드는 기본적으로 메소드 이름을 기반으로 쿼리를 생성해준다.
//    2. 메소드 이름에 특정 키워드를 사용해야한다.

//    조회는 find[식별자]By 를 사용한다.
//    findBy()는 기본적인 select from 쿼리를 생성해준다.
//    By앞에 붙는 [식별자]는 이 쿼리를 설명하는 내용이나 식별하기 위한 이름을 작성하면된다.(생략가능)
    List<Book> findBy();
    List<Book> findTestBy();
    List<Book> findBooksBy();

//    조건을 추가하고 싶다면 By뒤에 조건으로 사용할 필드를 작성한다.
//    find[식별자]By[필드]
//    해당 필드로 equal(=) 조건을 추가해준다.
    List<Book> findByName(String name);

//    엔티티에 존재하는 필드라면 모두 조건으로 사용 가능하다.
    List<Book> findTestByPrice(int price);

//    *반환타입
//    쿼리 메소드는 다양한 반환타입을 설정할 수 있다.
//    entity, List, Optional 등등을 사용할 수 있다.
//    옵셔널, 엔티티를 반환타입으로 설정하는 경우 조회결과가 2건이상이면 오류가 발생된다.(주의!)
//    조회결과가 0건이면 null이 반환된다.
    Optional<Book> findOptionalByName(String name);

    Book findBookByName(String name);

//    ========================================================================================
//    * 조건 설정
//    Equal(=) 조건이 아닌 다른 조건도 사용이 가능하다.
//    findBy필드[조건설정]
//    1. 비교 연산자
//    GreaterThan : 초과 조건
//    LessThan : 미만 조건
    List<Book> findByPriceGreaterThan(int price);
    List<Book> findByPriceLessThan(int price);
    
//    GreaterThanEqual : 이상
//    LessThanEqual : 이하
//    날짜 비교 조건도 가능하지만 권장하지 않는다.
    List<Book> findByReleaseDateGreaterThanEqual(LocalDate date);
    List<Book> findByReleaseDateLessThanEqual(LocalDate date);

//    After : 초과 조건
//    Before : 미만 조건
//    GreaterThan, LessThan과 동일하게 쿼리가 생성된다.
//    After, Before는 의미적으로 날짜 비교에 사용하는것을 권장한다.
    List<Book> findByReleaseDateAfter(LocalDate date);
    List<Book> findByReleaseDateBefore(LocalDate date);

//    2. like 문자열 조건 설정하기
//    Like : like 조건을 생성한다. -> like 'keyword'
    List<Book> findByNameLike(String keyword);

//    NotLike : not like 조건을 생성한다. -> not like 'keyword'
    List<Book> findByNameNotLike(String keyword);

//    Containing : like를 이용해 포함 조건을 생성한다. -> like '%keyword%'
    List<Book> findByNameContaining(String keyword);

//    StartingWith : like를 이용해 시작 글자 조건을 생성한다. -> like 'keyword%'
//    EndingWith : like를 이용해 마지막 글자 조건을 생성한다. -> like '%keyword'
    List<Book> findByNameStartingWith(String keyword);
    List<Book> findByNameStartsWith(String keyword);
    List<Book> findByNameEndingWith(String keyword);

//    3. Null 조건 설정하기
//    IsNull, IsNotNull
    List<Book> findByPriceIsNull();
    List<Book> findByPriceIsNotNull();

//    SQL 연산자 조건 설정하기
//    Between : Between 조건을 생성한다. -> between A and B
//    매개변수 2개 사용한다.
    List<Book> findByPriceBetween(int a, int b);

//    In : in 조건을 생성한다. -> in(a,b,c,.....)
//    매개변수에 Collection 타입을 사용한다.
//    Collection을 상속받은 List, Set타입도 가능하다.
    List<Book> findByNameIn(Collection<String> names);

//    NotIn : not in 조건을 생성한다. -> not in(a,b,c,....)
    List<Book> findByNameNotIn(Collection<String> names);

//    5. 논리연산자를 사용하여 여러 조건 사용하기
//    And, Or
//    findBy필드명[조건]And필드명[조건]

    List<Book> findByNameAndPrice(String name, int price);
    List<Book> findByNameAndPriceLessThanAndReleaseDateAfter(String name, int price, LocalDate date);
//  =========================================================================================
//    * 기타 키워드
//    1. 정렬하기
//    OrderBy필드명[Desc]
//    항상 메소드 이름의 마지막에 사용한다.
    List<Book> findByPriceGreaterThanOrderById(int price);
    List<Book> findByPriceGreaterThanOrderByIdDesc(int price);

//    2. 대소문자 무시하기
//    findBy필드명[조건]IgnoreCase -> where 컬럼 = upper('keyword')
    List<Book> findByNameIgnoreCase(String name);
    List<Book> findByNameIgnoreCaseAndPrice(String name, int price);

//    like와 함께 사용 가능
    List<Book> findByNameContainingIgnoreCase(String keyword);

//    3. 중복제거
//    findDistinct[식별자]By : select절에 distinct를 추가한다.
    List<Book> findDistinctByPrice(int price);

//    4. 상위 결과 가져오기 (Rank)
//    findTop<number>[식별자]By -> 조회 결과의 상위 <number>개를 가져온다.
    List<Book> findTop3ByName(String name);

//    OrderBy와 같이 사용하는 경우가 많다.
    List<Book> findTop2ByOrderByPriceDesc();

//    =======================================================================================
//    * find 외의 키워드
//    1. count[식별자]By[필드][조건] 
//   반환타입은 정수로 설정
    long countBy();
    long countByReleaseDateAfter(LocalDate date);

//    2. exists[식별자]By[필드][조건] : 조회 결과 유/무
    boolean existsBy();
    boolean existsByCategory(BookCategory bookCategory);

//    3. delete[식별자]By[필드][조건] : 조건으로 여러 행 삭제
    void deleteByReleaseDateAfter(LocalDate date);
}


















