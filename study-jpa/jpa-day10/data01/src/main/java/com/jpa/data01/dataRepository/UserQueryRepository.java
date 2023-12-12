package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.dto.UserDto;
import com.jpa.data01.domain.embedded.Address;
import com.jpa.data01.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface UserQueryRepository extends JpaRepository<User, Long> {
//    모든 메소드는 @Query와 JPQL을 사용한다.

    //    전체 회원의 이름 조회
    @Query("select u.name from User u")
    List<String> findUserName();

    //    핸드폰 번호가 010으로 시작하는 회원 조회 escape 모르면 검색해보기
    //    like '010%' escape '/'
    @Query("select u from User u where u.phone like '010%' escape '/'")
    List<User> findUsersByPhone();

    //    주소 상세를 전달 받아 일치하는 회원 조회 (예 : 101호)
    @Query("select u from User u where u.address.addressDetail = :addressDetail")
    List<User> findUsersByAddressDetail(@Param("addressDetail") String addressDetail);

    //    이름이 특정 글자로 시작하는 회원 조회(성으로 회원 조회)
    @Query("select u from User u where u.name like concat(:keyword, '%') escape '/'")
    List<User> findUsersByKeyword(@Param("keyword") String keyword);

    //    이름을 여러 개 전달 받아 해당 이름들과 일치하는 회원 조회
    @Query("select u from User u where u.name in (:keywords)")
    List<User> findUsersByKeywords(@Param("keywords") Collection<String> keywords);

    //    전체 회원의 주소, 주소 상세, 우편번호 조회
    @Query("select u.address from User u")
    List<Address> findAddress();

//    id, name, birth 필드를 가진 UserDto를 만든다.
//    -> 우편번호를 전달받아 부분 일치하는 회원 정보를 Dto로 반환 (예 : 1 -> 11111, 41233, 44412)
    @Query("select new com.jpa.data01.domain.dto.UserDto(u.id, u.name, u.birth) from User u " +
            "where u.address.zipcode like %:keyword% escape '/'")
    List<UserDto> findDtoByZipCode(@Param("keyword")String keyword);

//    회원의 주소는 강남구, 강북구, 강동구 3종류가 있다고 가정하고 각 주소별
//    나이가 가장 많은 사람의 주소(강남구, 강북구 등)와 생일을 Map으로 반환
    @Query("select new Map(u.address.address as address, min(u.birth) as birth) " +
            "from User u group by u.address.address")
    List<Map<String, Object>> findMap();

//    나이가 10살 이상인 회원 조회
//    @Query("select u from User u " +
//            "where function('to_char', current_timestamp, 'YYYY') " +
//            "- function('to_char', u.birth, 'YYYY') >= 10")
    @Query("select u from User u where year(current_timestamp) - year(u.birth) >= 12")
    List<User> findUserByAge();
}
