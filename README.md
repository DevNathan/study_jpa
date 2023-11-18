조남호 | Nathan-Cho<br>
07.10.23 ~ 진행중

# JPA
### 목차

***
## 1. JPA란?
    객체(JAVA)와 RDB의 패러다임 불일치 문제를 해소하기위해 RDB를 객체지향적으로 만들어 사용하자는
    취지에서 ORM이 탄생하였고 이 ORM을 사용하기 위한 JPA가 탄생하였다.

- ORM(Object Relational Mapping

      객체지향 언어와 관계형 DB와의 데이터를 매핑해주는 기술 (클래스와 테이블을 그대로 맵핑)
	    객체지향 언어와 관계형 DB는 서로 다른 구조를 가지고 있기 때문에
	    자바가 가지는 여러 이점을 제대로 사용할 수 없다.
	    이를 해결하고자 나온 기술이 테이블과 객체를 매핑시켜주는 ORM이다.
- JPA(JAVA Persistent API)

      자바에서 ORM을 사용하기 위한 표준 API(자바 전용)
      JPA는 인터페이스를 제공하는 역할을 하며 실질적인 ORM 기술을 구현해 놓은 것이 Hibernate같은 ORM
- Spring Data JPA

      JPA를 기반으로 만든 모듈이다
      Repository라는 인터페이스를 제공하여 DB와의 상호작용을 더 쉽고 간결하게 만들어준다.
      내부적으로는 JPA를 사용하기 때문에 JPA에 대한 선행학습이 필요하다.
- Hibernate

      ORM기술을 구현해 놓은 오픈소스 프레임워크이다.

