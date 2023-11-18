조남호 | Nathan-Cho<br>
07.10.23 ~ 진행중

# JPA
### 목차
1. [JPA란?](#1-jpa란)
2. [JPA 사용](#2-jpa-사용)
	> 2-1 [JPA 테이블 등록하기](#2-1-jpa-테이블-등록하기)<br>
	> 2-2 [CRUD](#2-2-crud)

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


***
## 2. JPA 사용
  		JPA와 관련하여 상당히 많은 수의 어노테이션이 추가되게 된다.
  		모든 어노테이션을 알필요는 없으나 중요한 어노테이션은 반드시 알아두고
  		JPA와 DB의 관계가 어떤식으로 되어있는지 이해가 중요하다.

### 2-1 JPA 테이블 등록하기
	테이블과 DB를 매핑시키기 위해서는 Entity어노테이션을 이용해 
 	Entity로 테이블을 등록시켜줘야 한다.

	아래는 테이블을 만들기 위해 사용할 수 있는 어노테이션들이며 
 	必은 반드시 사용해야하는 어노테이션이다.
 
@Entity(必)

	테이블을 Entity로 등록한다. 테이블 이름은 Entity의 이름과 동일하게 만들어진다.
  	엔티티를 만드는 순간 오류가 나게 되는 이유는
	테이블을 만들 때 반드시 필요한 PK가 없기 때문이다. 
	추가적으로 Entity는 기본생성자가 필수이다.
@Table

	엔티티와 매핑할 테이블을 지정한다. 
 	생략할 시 매핑한 엔티티의 이름을 테이블 이름으로 사용한다.

	name = 매핑할 테이블 이름
@Id(必)

	해당 필드를 PK칼럼으로 등록한다.
@GeneratedValue

	해당 컬럼에 자동 증가되는 값이 들어가게 된다.
@Column

	객체 필드를 테이블 컬럼에 매핑한다.

 	name : 테이블 컬럼의 이름
  	nullable : null값의 허용 여부를 결정한다. (true/false)
   	unique : 유니크 제약조건을 적용한다. (true/false)
	length : 문자 길이 제약조건이며 String 타입에만 사용한다. (Default 225)
	precision, scale : 숫자의 형태를 조절한다. BigDecimal 타입에서 사용한다.
				precision은 소숫점을 포함한 전체 자릿수이며
				scale은 소수의 자릿수이다.

@Enumerated

	JAVA의 enum 타입을 컬럼에 매핑하기위해 사용한다.

	EnumType.ORDINAL : enum 순서를 값으로 DB에 저장한다.
 	EnumType.STRING : enum 이름을 값으로 DB에 저장한다.
@Lob

	데이터베이스의 BLOB, CLOB, TEXT 타입과 매핑이 된다.

### 2-2 CRUD
JPA를 통해서 CRUD하기 위해서는 JPA에 대한 구조적 이해가 필요하다.<br>
이는 [3. JPA 구조](#3-jpa-구조)에서 확인한다.

@PersistenceContext

	영속성컨텍스트를 사용할 수 있도록 한다.
EntityMangerFactory

	EntityManager를 만들고 구성하는 법을 제공하는 interface이다
EntityManager

	DB 테이블과 매핑된 Entity에 대한 CRUD를 수행하는 method를 제공하며 
 	Entity의 영속성과 생명주기를 관리, 담당한다.
- 
  
***
## 3. JPA 구조
