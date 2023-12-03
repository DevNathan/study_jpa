package com.jpa.task.service;

import com.jpa.task.domain.embedded.Address;
import com.jpa.task.domain.entity.User;
import com.jpa.task.domain.enumType.UserGender;
import com.jpa.task.exception.DuplicateUserException;
import com.jpa.task.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// Junit5에서 지원하는 어노테이션이며 테스트를 진행할 때 필요한 확장기능을 설정하는 어노테이션이다.
//    Mock 객체를 사용하는 이유
//    Service를 테스트하고자 하는데 Repository까지 테스트를 한다면 불필요한 테스트가 진행되는 것이다.
//    그러므로 실제 Repository 객체를 만들어 컨테이너에 올릴 필요가 없다.
//    테스트하고자 하는 Service객체만 만들고 나머지는 Mock(가짜)객체를 사용하여 테스트를 진행해야 단위 테스트의 목적에 맞다.
class UserServiceTest {
    @Mock // 모방하는 객체를 생성한다. (가짜 UserRepository 객체)
            // Mock 객체는 껍데기만 있고 내부에 로직은 존재하지 않는다(선언부만 존재하고 내부 코드는 존재하지 않음).
    UserRepository userRepository;

    @InjectMocks // 서비스 객체는 레포지토리를 주입받아야만 한다.
            // 위에서 만든 Mock 객체를 서비스에 주입한다.
    UserService userService;
    User user;

    @BeforeEach
    void setup(){
        user = new User();
        user.setLoginId("bbb");
        user.setName("홍길동");
        user.setPassword("1234");
        user.setGender(UserGender.M);
        user.setAddress(new Address("강남구", "100호", "11111"));
    }

    @Test @DisplayName("회원가입 테스트")
    void join() throws DuplicateUserException {
//        userRepository는 가짜 객체이므로 각 메소드의 껍데기만 존재하고 내부에 실제 코드는 없다.
//        그렇기 때문에 해당 메소드를 호출하면 아무일도 일어나지 않는다.
//        개발자는 테스트를 위해 doReturn(), when() 등을 이용하여 메소드를 테스트 목적에 맞게 정의할 수 있다(스터빙).

//        given (준비)
//        레포지토리의 save() 메소드를 실행시킬 때 아무 값이든지 들어오게 될 시 user 반환
        doReturn(user).when(userRepository).save(any());
        doReturn(Optional.empty()).when(userRepository).findByLoginId(any());
//        when (실행)
        User savedUser = userService.join(user);
//        then (검증)
        assertThat(savedUser.getName()).isEqualTo(user.getName());
    }

    @Test @DisplayName("회원가입 테스트-중복처리")
    void joinDuplicateUser(){
//        given
        doReturn(Optional.of(user)).when(userRepository).findByLoginId(any());

//        when

//        then
        assertThatThrownBy(() -> userService.join(user))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("중복된");
    }

    @Test @DisplayName("로그인")
    void login() {
//        given
        doReturn(Optional.of(1L)).when(userRepository).findByLoginIdAndPassword(any(), any());

//        when
        Long foundId = userService.login("", "");

//        then
        assertThat(foundId).isNotNull().isEqualTo(1L);
    }

    @Test @DisplayName("로그인 예외 확인")
    void loginException(){
//        given
        doReturn(Optional.empty()).when(userRepository).findByLoginIdAndPassword(any(), any());

//        when
//        then
        assertThatThrownBy(() -> userService.login("", ""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지");
    }
}