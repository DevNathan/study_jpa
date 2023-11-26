package com.jpa.task.optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.Optional;

@SpringBootTest
public class OptionalTest {

    @Test
    void exam01(){
        /*
        옵셔널 클래스는 NPE를 방어하기 위해 사용한다.
        개발을 할 때 가장 많이 발생하는 예외 중 하나가 NPE이다.
        참조변수에 들어있는 값이 null인경우 접근 연산자(.)를 이용하여 맴버에 접근하면 NPE가 발생할 수 있다.
        if문을 이용하여 null체크를 하게되면 경우의수가 너무 많아서 개발이 피곤해진다.
        Optional타입은 null체크를 보다 간결하고 안전하게 처리할 수 있다.
         */

//        1. empty()
//        비어있는 옵셔널 객체를 생성 (사용빈도 낮음)
        Optional<Object> empty = Optional.empty();
//        2. of()
//        값을 저장하고 있는 옵셔널 객체를 생성한다. (사용빈도 낮음)
//        값이 확실하게 null이 아닌경우에만 사용한다. 만약 null을 저장하면 NPE가 발생한다.
        Optional<Long> aLong = Optional.of(3L);
//        3. ofNullable()
//        값을 저장하고 있는 옵셔널 객체를 생성한다.
//        값이 null일 수도 있는 경우 사용한다.
        Optional<Object> o = Optional.ofNullable(null);
    }

    @Test
    void exam02(){
        Optional<Object> o = Optional.ofNullable(null);

//        1. get()
//        저장된 값을 반환한다. null인 경우 예외 발생
//        Object o1 = o.get();

//        2. orElse(대체 값)
//        옵셔널 객체에 저장된 값을 반환한다. null인 경우 [대체 값]이 반환된다.
        Object o1 = o.orElse(3L);
        System.out.println(o1);

//        3. orElseGet(람다식)
//        옵셔널 객체에 저장된 값을 반환한다. null인 경우 [람다식]이 실행된다.
        o.orElseGet(() -> 3L);

//        4. orElseThrow(특정 예외)
//        옵셔널 객체에 저장된 값을 반환한다. null인 경우 [특정 예외]를 발생시킨다.
//        o.orElseThrow(() -> new IllegalStateException());

//        5. isPresent()
//        옵셔널 객체에 저장된 값이 null이면 false, 아니면 true를 반환 (조건식으로 사용)
        o.isPresent();

//        6. ifPresentOrElse(람다식, 람다식)
//        null이 아닌경우에 실행할 람다식과 null인 경우에 실행할 람다식
        o.ifPresentOrElse(
                obj -> System.out.println(obj),
                () -> System.out.println("null이다!")
        );
    }
}






