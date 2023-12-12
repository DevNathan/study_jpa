package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<User, Long> {
//    핸드폰 번호로 조회
    Optional<User> findByPhone(String phone);

    //    생일이 특정 날짜보다 이전인 회원 조회
    List<User> findByBirthBefore(LocalDate birthDay);

    //    이름에 특정 글자가 포함되는 회원 조회
    List<User> findByNameContaining(String keyword);

    //    이름에 특정 글자가 포함되는 회원의 수를 조회
    long countByNameContaining(String keyword);

    //    특정 핸드폰 번호가 존재하는지 검사
    boolean existsByPhone(String phone);

    //    이름에 특정 글자가 포함되고 핸드폰에 특정 글자가 포함되는 회원들을 id 내림차순으로 정렬하여 조회
    List<User> findByNameContainingAndPhoneContainingOrderByIdDesc(String nameKeyword, String phoneKeyword);

//   여러 이름을 넘겨받아 해당 이름과 일치하는 User 전체 정보 모두 조회하기
    List<User> findByNameIn(Collection<String> names);

//   birth가 null이 아니고 id가 특정 범위안에 속하는 회원 모두 조회
    List<User> findByBirthNotNullAndIdBetween(Long start, Long end);
}
